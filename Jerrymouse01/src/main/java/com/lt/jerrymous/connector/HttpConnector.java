package com.lt.jerrymous.connector;

import com.lt.jerrymous.engine.HttpServletRequestImpl;
import com.lt.jerrymous.engine.HttpServletResponseImpl;
import com.lt.jerrymous.engine.ServletContextImpl;
//import com.lt.jerrymous.engine.filter.HelloFilter;
import com.lt.jerrymous.engine.filter.LogFilter;
import com.lt.jerrymous.engine.listener.*;
import com.lt.jerrymous.engine.servlet.HelloServlet;
import com.lt.jerrymous.engine.servlet.IndexServlet;
import com.lt.jerrymous.engine.servlet.LoginServlet;
import com.lt.jerrymous.engine.servlet.LogoutServlet;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import jakarta.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.EventListener;
import java.util.List;

/**
 * 自定义、改造HTTP请求的HttpHandler接口
 * AutoCloseable 确保资源被正确的释放。如文件操作、网络连接、数据库连接等
 * 将Java内置的HttP 服务器的请求和响应对象与“Servlet API”对象桥接起来，
 * 以便能在Tomcat服务器中使用Servlet风格的编程模式
 */
public class HttpConnector implements HttpHandler, AutoCloseable {

    final Logger logger = LoggerFactory.getLogger(getClass());

    final ServletContextImpl servletContext;
    final HttpServer httpServer;
    final Duration stopDelay = Duration.ofSeconds(5);

    public HttpConnector() throws IOException {
        this.servletContext = new ServletContextImpl();
        this.servletContext.initServlets(List.of(IndexServlet.class, LoginServlet.class, LogoutServlet.class, HelloServlet.class));
        this.servletContext.initFilters(List.of(LogFilter.class));
        List<Class<? extends EventListener>> listenerClasses = List.of(HelloHttpSessionAttributeListener.class, HelloHttpSessionListener.class,
                HelloServletContextAttributeListener.class, HelloServletContextListener.class, HelloServletRequestAttributeListener.class,
                HelloServletRequestListener.class);
        for (Class<? extends EventListener> listenerClass : listenerClasses) {
            this.servletContext.addListener(listenerClass);
        }
        // start http server:
        String host = "0.0.0.0";
        int port = 8080;
        this.httpServer = HttpServer.create(new InetSocketAddress(host, port), 0, "/", this);
        this.httpServer.start();
        logger.info("jerrymouse http server started at {}:{}...", host, port);
    }

    @Override
    public void close() {
        this.httpServer.stop((int) this.stopDelay.toSeconds());
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var adapter = new HttpExchangeAdapter(exchange);
        var response = new HttpServletResponseImpl(adapter);
        var request = new HttpServletRequestImpl(this.servletContext, adapter, response);
        // process:
        try {
            this.servletContext.process(request, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            response.cleanup();
        }
    }
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

