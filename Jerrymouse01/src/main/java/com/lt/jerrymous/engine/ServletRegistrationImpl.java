package com.lt.jerrymous.engine;

import com.lt.jerrymous.engine.support.InitParameters;
import jakarta.servlet.*;

import java.util.*;

//动态注册 Servlets
//即在应用程序启动时或者运行时动态添加 Servlet 而不是通过 web.xml 文件静态配置
public class ServletRegistrationImpl implements ServletRegistration.Dynamic {

    public final ServletContext servletContext;//表示当前 Servlet 所属的上下文环境（ServletContext），用于在应用范围内共享数据。
    final String name;//表示 Servlet 的名称
    public final Servlet servlet;//需要注册的 Servlet 实例
    final List<String> urlPatterns = new ArrayList<>(4);//存储 Servlet 的 URL 映射（即 Servlet 对应的请求路径）。

    public boolean initialized = false;//标志 Servlet 是否已初始化

    public ServletRegistrationImpl(ServletContext servletContext, String name, Servlet servlet) {
        this.servletContext = servletContext;
        this.name = name;
        this.servlet = servlet;
    }

    public ServletConfig getServletConfig() {
        return new ServletConfig() {
            @Override
            public String getServletName() {
                return ServletRegistrationImpl.this.name;
            }

            @Override
            public ServletContext getServletContext() {
                return ServletRegistrationImpl.this.servletContext;
            }

            @Override
            public String getInitParameter(String name) {
                return null;
            }

            @Override
            public Enumeration<String> getInitParameterNames() {
                return null;
            }
        };
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getClassName() {
        return servlet.getClass().getName();
    }

//    用于为 Servlet 添加 URL 映射（即请求路径）。这些路径将与 Servlet 关联，以便处理对应的请求。
    @Override
    public Set<String> addMapping(String... urlPatterns) {
        if (urlPatterns == null || urlPatterns.length == 0) {
            throw new IllegalArgumentException("Missing urlPatterns.");
        }
        for (String urlPattern : urlPatterns) {
            this.urlPatterns.add(urlPattern);
        }
        return Set.of();
    }

    @Override
    public Collection<String> getMappings() {
        return this.urlPatterns;
    }

    @Override
    public String getRunAsRole() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAsyncSupported(boolean isAsyncSupported) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setLoadOnStartup(int loadOnStartup) {
        // TODO Auto-generated method stub
    }

    @Override
    public Set<String> setServletSecurity(ServletSecurityElement constraint) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setMultipartConfig(MultipartConfigElement multipartConfig) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setRunAsRole(String roleName) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean setInitParameter(String name, String value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getInitParameter(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> setInitParameters(Map<String, String> initParameters) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, String> getInitParameters() {
        // TODO Auto-generated method stub
        return null;
    }
}
