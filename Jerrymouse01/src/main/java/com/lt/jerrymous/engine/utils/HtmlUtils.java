package com.lt.jerrymous.engine.utils;

//对字符串进行HTML编码
public class HtmlUtils {
    public static String encodeHtml(String s) {
        return s.replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;").replace("\"", "&quot;");
    }
}
