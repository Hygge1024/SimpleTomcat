package com.lt.jerrymous.connector;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

public class HttpExchangeAdapter implements HttpExchangeRequest, HttpExchangeResponse{
    /*
    装入自己 要适配的类
     */
    final HttpExchange exchange;
    byte[] requestBodyData;

    public HttpExchangeAdapter(HttpExchange exchange){
        this.exchange = exchange;
    }


    /**
     * 通过调用 Imterface定义的类来调用HttpExchange的类
     */

    @Override
    public String getReqeustMethod() {
        return this.exchange.getRequestMethod();
    }

    @Override
    public URI getRequestURI() {
        return this.exchange.getRequestURI();
    }

    @Override
    public Headers getRequestHeaders() {
        return this.exchange.getRequestHeaders();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return this.exchange.getRemoteAddress();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return this.exchange.getLocalAddress();
    }

    @Override
    public byte[] getRequestBody() throws IOException {
        if(this.requestBodyData == null){//为空表示还没有读取过请求的主题内容
            try(InputStream input = this.exchange.getRequestBody()){//获取请求的输入流，保证操作完毕后自动关闭输入流
                this.requestBodyData = input.readAllBytes();//从输入流中读取所有字节。
            }
        }
        return this.requestBodyData;
    }

    @Override
    public Headers getResponseHeaders() {
        return this.exchange.getResponseHeaders();
    }

    @Override
    public void sendResponseHeaders(int rCode, long responseLength) throws IOException {
        this.exchange.sendResponseHeaders(rCode,responseLength);//实现依靠的是exchange的sendResponseHeaders()方法。
    }

    @Override
    public OutputStream getResponseBody() {
        return this.exchange.getResponseBody();
    }
}
