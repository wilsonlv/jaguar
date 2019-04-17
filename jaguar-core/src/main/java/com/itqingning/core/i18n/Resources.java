package com.itqingning.core.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 加载配置
 *
 * @author lvws
 * @version 2016年5月20日 下午3:19:19
 */
public final class Resources {

    /**
     * 国际化信息
     */
    private static final Map<String, ResourceBundle> MESSAGES = new HashMap<String, ResourceBundle>();

    /**
     * 国际化信息
     */
    public static String getMessage(String key, Object... params) {
        Locale locale = Locale.CHINA;
        ResourceBundle message = MESSAGES.get(locale.getLanguage());
        if (message == null) {
            synchronized (MESSAGES) {
                message = MESSAGES.get(locale.getLanguage());
                if (message == null) {
                    message = ResourceBundle.getBundle("i18n/messages", locale);
                    MESSAGES.put(locale.getLanguage(), message);
                }
            }
        }
        if (params != null && params.length > 0) {
            String formatMessage = String.format(message.getString(key), params);
            return formatMessage;
        }
        return message.getString(key);
    }

    /**
     * 清除国际化信息
     */
    public static void flushMessage() {
        MESSAGES.clear();
    }
}
