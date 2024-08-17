package com.lt.jerrymous.engine.mapping;

import jakarta.servlet.Servlet;

import java.util.regex.Pattern;

public class ServletMapping extends AbstractMapping{
    public final Servlet servlet;// Servlet实例

    public ServletMapping(String urlPattern, Servlet servlet) {
        super(urlPattern);//调用父类的方法
        this.servlet = servlet;
    }
}
