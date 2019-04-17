package com.itqingning.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by apple on 2018/11/8.
 */
public class DateUtil {

    /**
     * 日期格式
     **/
    public enum DatePattern {

        HHMMSS("HHmmss"),
        HH_MM_SS("HH:mm:ss"),
        HH_MM_SS_SSS("HH:mm:ss.SSS"),

        YYYYMMDD("yyyyMMdd"),
        YYYY_MM_DD("yyyy-MM-dd"),
        YYYYMMDDHHMMSS("yyyyMMddHHmmss"),
        YYYYMMDDHHMMSSSSS("yyyyMMddHHmmssSSS"),
        YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss");

        private String pattern;

        DatePattern(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public String toString() {
            return pattern;
        }
    }


    public enum CalendarField {

        YEAR(1),
        MONTH(2),
        DATE(5),
        HOUR(11),
        MINUTE(12),
        SECOND(13);

        private int field;

        CalendarField(int field) {
            this.field = field;
        }

        public int get() {
            return field;
        }
    }

    /**
     * 根据格式自动转化
     */
    public static Date autoParse(String date) {
        if (date == null || date.trim().equals("")) {
            return null;
        }
        String separator = String.valueOf(date.charAt(4));
        String pattern = "yyyyMMdd";
        if (!separator.matches("\\d*")) {
            pattern = "yyyy" + separator + "MM" + separator + "dd";
            if (date.length() < 10) {
                pattern = "yyyy" + separator + "M" + separator + "d";
            }
        } else if (date.length() < 8) {
            pattern = "yyyyMd";
        }
        pattern += " HH:mm:ss.SSS";
        pattern = pattern.substring(0, Math.min(pattern.length(), date.length()));
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 格式化日期 yyyy-MM-dd
     */
    public static String format(Object date) {
        return format(date, DatePattern.YYYY_MM_DD);
    }

    /**
     * 格式化日期
     */
    public static String format(Object date, DatePattern pattern) {
        if (date == null) {
            return null;
        }
        if (pattern == null) {
            return format(date);
        }
        return new SimpleDateFormat(pattern.toString()).format(date);
    }

    /**
     * 格式化日期
     */
    public static String format(Object date, String pattern) {
        if (date == null) {
            return null;
        }
        if (pattern == null) {
            return format(date);
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 转化为日期格式 yyyy-MM-dd
     */
    public static Date parse(String date) {
        return parse(date, DatePattern.YYYY_MM_DD);
    }

    /**
     * 转化为日期格式
     */
    public static Date parse(String date, DatePattern pattern) {
        if (date == null) {
            return null;
        }
        if (pattern == null) {
            return parse(date);
        }

        try {
            return new SimpleDateFormat(pattern.toString()).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前日期 yyyy-MM-dd
     */
    public static String getDate() {
        return format(new Date());
    }

    /**
     * 获取当前日期时间 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getDateTime() {
        return format(new Date(), DatePattern.YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取当前日期时间
     *
     * @param pattern
     * @return
     */
    public static String getDateTime(DatePattern pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 获取当前日期时间
     *
     * @param pattern
     * @return
     */
    public static String getDateTime(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 日期计算
     */
    public static Date addDate(Date date, CalendarField field, int amount) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field.get(), amount);
        return calendar.getTime();
    }

    /**
     * 获取今天的开始时间
     */
    public static Date getCurrentDayStartTime() {
        return getTargetDayStartTime(new Date());
    }

    /**
     * 获取今天的结束时间
     */
    public static Date getCurrentDayEndTime() {
        return getTargetDayEndTime(new Date());
    }

    /**
     * 获取指定日期的开始时间
     */
    public static Date getTargetDayStartTime(Date date) {
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        instance.setTime(date);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        return instance.getTime();
    }

    /**
     * 获取指定日期的结束时间
     */
    public static Date getTargetDayEndTime(Date date) {
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        instance.setTime(date);
        instance.set(Calendar.HOUR_OF_DAY, 23);
        instance.set(Calendar.MINUTE, 59);
        instance.set(Calendar.SECOND, 59);
        return instance.getTime();
    }

    /**
     * 间隔天数
     */
    public static int getDiffDay(Date startDate, Date endDate) {
        Date startTime = getTargetDayStartTime(startDate);
        Date endTime = getTargetDayStartTime(endDate);
        return (int) ((endTime.getTime() - startTime.getTime()) / 1000 / 3600 / 24);
    }

}
