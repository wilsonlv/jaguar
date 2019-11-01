package org.jaguar.commons.utils;

import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;


/**
 * Created by lvws on 2019/10/31.
 */
public class DateUtil {

    /**
     * 日期格式
     **/
    public enum DateTimePattern {

        HHMMSS("HHmmss"),
        HH_MM_SS("HH:mm:ss"),
        HH_MM_SS_SSS("HH:mm:ss.SSS"),

        YYYYMMDD("yyyyMMdd"),
        YYYY_MM_DD("yyyy-MM-dd"),

        YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
        YYYY_MM_DD_HH_MM_SS_SSS("yyyy-MM-dd HH:mm:ss.SSS");

        private String pattern;

        DateTimePattern(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public String toString() {
            return pattern;
        }
    }

    public static String formatDate(Date date) {
        return format(date, DateTimePattern.YYYY_MM_DD);
    }

    public static String formatTime(Date date) {
        return format(date, DateTimePattern.HH_MM_SS);
    }

    public static String formatDateTime(Date date) {
        return format(date, DateTimePattern.YYYY_MM_DD_HH_MM_SS);
    }

    public static String format(Date date, DateTimePattern dateTimePattern) {
        return FastDateFormat.getInstance(dateTimePattern.pattern).format(date);
    }


    public static String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DateTimePattern.YYYY_MM_DD.pattern));
    }

    public static String formatTime(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern(DateTimePattern.YYYY_MM_DD_HH_MM_SS.pattern));
    }

    public static String formatDateTime(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern(DateTimePattern.HH_MM_SS.pattern));
    }

    public static String formatDateTime(LocalDateTime time, DateTimePattern dateTimePattern) {
        return time.format(DateTimeFormatter.ofPattern(dateTimePattern.pattern));
    }


    public static Date parse(String date) throws ParseException {
        return FastDateFormat.getInstance().parse(date);
    }

    public static Date parse(String date, DateTimePattern dateTimePattern) throws ParseException {
        return FastDateFormat.getInstance(dateTimePattern.pattern).parse(date);
    }


    public static LocalDate date2LocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalTime date2LocalTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}
