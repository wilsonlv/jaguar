package com.itqingning.jaguar.core.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by lvws on 2019/4/2.
 */
public class NumberUtil {

    /**
     * 字符串数字递增
     */
    public static String autoIncrease(String num) {
        if (StringUtils.isBlank(num)) {
            throw new IllegalArgumentException(num);
        }

        long l = Long.parseLong(num);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < num.length(); i++) {
            builder.append("0");
        }

        return builder.append(++l).substring(builder.length() - num.length());
    }

    public static String decrease(String num) {
        if (StringUtils.isBlank(num)) {
            throw new IllegalArgumentException(num);
        }

        long l = Long.parseLong(num);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < num.length(); i++) {
            builder.append("0");
        }

        return builder.append(--l).substring(builder.length() - num.length());
    }

    public static void main(String[] args) {
        String s = autoIncrease("0001");
        System.out.println(s);

        String decrease = decrease("0001");
        System.out.println(decrease);
    }

}
