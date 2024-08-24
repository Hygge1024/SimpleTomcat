//package com.lt.jerrymous.engine.filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.util.Set;
//
//@WebFilter(urlPatterns = "/hello")
//public class HelloFilter implements Filter {
//
//    final Logger logger = LoggerFactory.getLogger(getClass());
//
//    Set<String> names = Set.of("Bob", "Alice", "Tom", "Jerry");
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) request;
//        String name = req.getParameter("name");
//        logger.info("Check parameter name = {}", name);
//        if (name != null && names.contains(name)) {
//            chain.doFilter(request, response);
//            logger.info("Check is OK parameter name = {}", name);
//        } else {
//            logger.info("Check name = {}，我已进入else{} ， 即将403", name);
//            HttpServletResponse resp = (HttpServletResponse) response;
//            resp.sendError(403, "Forbidden");
//            logger.info("403已经结束");
//        }
//    }
//}
