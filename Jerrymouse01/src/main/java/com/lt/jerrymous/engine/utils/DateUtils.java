package com.lt.jerrymous.engine.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

//处理和格式化 GMT（格林尼治标准时间）时区的日期和时间
public class DateUtils {
    static final ZoneId GMT = ZoneId.of("Z");

    //将该字符串解析为一个 ZonedDateTime 对象
    public static long parseDateTimeGMT(String s){
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(s, DateTimeFormatter.RFC_1123_DATE_TIME);
        return zonedDateTime.toInstant().toEpochMilli();
    }
//    法接受一个长整型的时间戳 ts，表示的是自 Unix 纪元以来的毫秒数。
    public static String formatDateTimeGMT(long ts){
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(ts),GMT);
        return zonedDateTime.format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }
}
