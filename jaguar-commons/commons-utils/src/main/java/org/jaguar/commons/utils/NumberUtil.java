package org.jaguar.commons.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author lvws
 * @since 2019/4/2.
 */
public class NumberUtil {

    /**
     * 字符串数字自增
     */
    public static String increase(String num) {
        return increase(num, 1);
    }

    /**
     * 字符串数字按步数递增
     *
     * @param num  字符串数字
     * @param step 步数
     * @return 结果
     */
    public static String increase(String num, int step) {
        if (StringUtils.isBlank(num)) {
            throw new IllegalArgumentException(num);
        }

        long l = Long.parseLong(num);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < num.length(); i++) {
            builder.append("0");
        }

        return builder.append(l + step).substring(builder.length() - num.length());
    }

    /**
     * 字符串数字自减
     */
    public static String decrease(String num) {
        return decrease(num, 1);
    }

    /**
     * 字符串数字按步数递减
     *
     * @param num  字符串数字
     * @param step 步数
     * @return 结果
     */
    public static String decrease(String num, int step) {
        if (StringUtils.isBlank(num)) {
            throw new IllegalArgumentException(num);
        }

        long l = Long.parseLong(num);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < num.length(); i++) {
            builder.append("0");
        }

        return builder.append(l - step).substring(builder.length() - num.length());
    }

}
