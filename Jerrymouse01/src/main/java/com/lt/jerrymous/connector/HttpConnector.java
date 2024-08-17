package com.lt.jerrymous.connector;

import com.lt.jerrymous.engine.HttpServletRequestImpl;
import com.lt.jerrymous.engine.HttpServletResponseImpl;
import com.lt.jerrymous.engine.ServletContextImpl;
import com.lt.jerrymous.engine.servlet.HelloServlet;
import com.lt.jerrymous.engine.servlet.IndexServlet;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;

/**
 * 自定义、改造HTTP请求的HttpHandler接口
 * AutoCloseable 确保资源被正确的释放。如文件操作、网络连接、数据库连接等
 * 将Java内置的HttP 服务器的请求和响应对象与“Servlet API”对象桥接起来，
 * 以便能在Tomcat服务器中使用Servlet风格的编程模式
 */
public class HttpConnector implements HttpHandler,AutoCloseable {
    final Logger logger = LoggerFactory.getLogger(getClass());
    final HttpServer httpServer;//关键点，HttpServer服务
    final Duration stopDelay = Duration.ofSeconds(5);
    final ServletContextImpl servletContext;

    public HttpConnector() throws IOException, ServletException {
        this.servletContext = new ServletContextImpl();
        this.servletContext.initialize(List.of(IndexServlet.class, HelloServlet.class));
        String host = "0.0.0.0";
        int port = 8080;
        this.httpServer = HttpServer.create(new InetSocketAddress(host,port),0,"/",this);
        this.httpServer.start();
        logger.info("jerrymouse http server started at {}:{}...",host,port);

    }

    /**
     *毕竟是实现
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.info("{}:{} ? {}",exchange.getRequestMethod(),exchange.getRequestURI().getPath(),exchange.getRequestURI().getRawQuery());
        var adapter = new HttpExchangeAdapter(exchange);//将本地的这个Exchange转入适配器中
        var request = new HttpServletRequestImpl(adapter);
        var response = new HttpServletResponseImpl(adapter);
        try {
//            process(request,response);
            this.servletContext.process(request,response);
        }catch (Exception e){
            logger.error(String.valueOf(e.getClass()),e);
        }
    }

    @Override
    public void close() throws Exception {
        this.httpServer.stop(3);
    }

    /**
     * 用于处理HTTTP请求，并生成一个简单的HTML响应。
     */
//    void process(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
//        /**
//         * 这里虽然是调用的HttpServlet相关的接口，实际上底层还是调用的Excahnge的具体实现方法。
//         */
//        String name = request.getParameter("name");
//        System.out.println("name is === " + name);
//        String html = "<h1>Hello, " + (name == null ? "world" : name) + ".</h1>";
//        response.setContentType("text/html");
//        PrintWriter pw = response.getWriter();
//        pw.write(html);
//        pw.close();
//    }
}
