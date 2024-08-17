package com.lt.jerrymous;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
/**
 * HttpServer可以创建一个监听特定端口和地址的Http服务器实例，服务器可以再指定的端口上接受来自客户端的HTTP请求。
 * 也可以注册多个HttpHandler实例，用于处理不同的路径
 * 支持Http功能，如GET、POST、PUT、DELETE等请求方法
 *
 * HttpServer处理流程：
 * 1.start()方法之后，HttpServer实例就开始在指定的端口上监听来自客户端的HTTP请求
 * 2.自动监听：只有使用stop()方法后or主程序挂掉后才停止监听。
 * 3.请求到达：当客户端的请求到达服务器时，服务器会检查请求的URL路径
 * 4.调用处理器：每个到达的请求，服务器会根据请求的URL路径调用相应的HttpHandler实例的handle()方法处理请求，如果请求的
 * 路径与组测的HttpHandler的路径匹配，就会调用该HttpHandler的handler()方法
 * 5.返回响应：在handler()方法中，处理完请求后，通过调用HttpExchange实例的sendResponseHeaders()方法发送响应状态码和响应头，
 *
 */



/**
 * HttpHandler:用于处理Http请求
 * AutoCloseable:用于确保资源在不再需要时关闭->close方法
 */
public class SimpleHttpServer implements HttpHandler, AutoCloseable {

    /**
     * 日志记录器:日志记录器实例，用于记录日志信息
     */
    final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 成员变量
     */
    final HttpServer httpServer;    //表示HTTP服务器
    final String host;
    final int port;

    public static void main(String[] args) {
        String host = "0.0.0.0";
        int port = 8080;
        /**
         * 创建了SimpleHttpServer实例，
         */
        try(SimpleHttpServer connector = new SimpleHttpServer(host,port)){
            /**
             * 无限循环，每个一面休息依次。其目的是保持服务器的运行
             */
            for (;;){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }



    /**
     * 构造方法——有参构造
     * @param host 主机地址
     * @param port 端口
     * @throws IOException 抛出IO异常
     */
    public SimpleHttpServer(String host,int port) throws IOException {
        this.host = host;
        this.port = port;
        /**
         * 将成员变量httpServer进行了初始化，创建了一个新的上下文，路径为根路径'/'，
         * 并将当前的SimpleHttpServer ——> HttpHandler实例作为请求处理器。
         * InetSocketAddress 是一个网络地址类，它表示一个IP地址和端口号的组合。
         * 参数0:后备队列长度，指定了请求处理线程可以排队等待处理的请求数量。
         * 根路径"/"，这意味着这个HttpHandler将处理发送到服务器根的请求。
         * this:这个参数是一个实现HttpHandler接口的对象。HttpHandler接口包含一个handle方法，该方法会被调用以处理HTTP请求
         */
        this.httpServer = HttpServer.create(new InetSocketAddress("0.0.0.0",8080),0,"/",this);
        //此处也可以单独使用.createContext()方法去创建多个context实例去处理各自的web应用，需要 “/diffrence”

        /**
         * 启动服务器
         */
        this.httpServer.start();
        /**
         * 记录日志
         */
        logger.info("start jerrymouse http server at {}:{}",host,port);
    }


    /**
     * 当前处理实例SimpleHttpServer的handle()方法。
     *
     * @param exchange the exchange containing the request from the
     *                 client and used to send the response 传入的请求与响应
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        /**
         * 获取请求的方法（如GET、POST）
         * URL、路径、和查询参数
         */
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String query = uri.getRawQuery(); //查询参数
        /**
         * 参数日志记录
         * http://localhost:8080/api/corpus?apple=iphone15
         * 请求方式：GET: 路径：/api/corpus?参数：apple=iphone15
         */
        logger.info("请求方式：{}: 路径：{}?参数：{}",method,path,query);
        /**
         * 创建响应相关信息
         */
        Headers respHeaders = exchange.getResponseHeaders();
        respHeaders.set("Context-Type","text/html;charset=utf-8"); //响应头
        respHeaders.set("Cache-Control","no-cache"); //响应缓存机制
        //设置200响应
        exchange.sendResponseHeaders(200,0);
        /**
         * 创建一个简单的HTML响应，包括当前的时间
         */
        String s = "<h1>Hello , World. --By Tara</h1><p>" + LocalDateTime.now().withNano(0) + "</p>";
        /**
         * 将响应内容写入输入流——写入响应体
         * exchange.getResonseBody() 获取响应的主体body内容。将其转存为OutputStream对象，方便改写操作
         * 后续有HttpServer自动返回给客户端。
         */
        try(OutputStream outputStream = exchange.getResponseBody()){
            outputStream.write(s.getBytes(StandardCharsets.UTF_8));
        }
        // try-with-resources 语句自动关闭流
        // 注意：流关闭后，HttpServer会自动将响应数据发送给客户端

        /**
         * 特别强调：
         * 如果不在代码中显示的写入响应体并关闭输出流，HttpServer不会自动返回任何信息给客户端，在调用HttpExchange的
         * sendResponseHeaders方法发送响应头和状态码之后，必须手动写入响应体，然后关闭输出流，才能让HttpServer将
         * 响应数据发送给客户端。
         *
         * 如果省略了写入响应体和关闭流的步骤，客户端可能会遇到以下情况：
         * 客户端可能会等待服务器发送数据，但由于服务器没有发送任何数据，客户端可能会超时。
         * 在某些情况下，客户端可能会收到一个空的响应或者不完整的响应，这取决于服务器的具体实现和客户端的行为。
         *
         */
    }

    /**
     * 调用HttpServer实例的stop方法停止服务器，3表示3秒内完成停止操作
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        this.httpServer.stop(3);
    }
}
