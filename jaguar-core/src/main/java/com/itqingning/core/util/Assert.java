package com.itqingning.core.util;

import com.itqingning.core.exception.BusinessException;
import com.itqingning.core.i18n.Resources;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lvws
 * @since 2019年2月27日
 */
public final class Assert {

    private static final String VALIDATE_ID = "VALIDATE_ID";
    private static final String NOT_NULL = "NOT_NULL";
    private static final String DUPLICATE = "DUPLICATE";

    private static String getMessage(String key, Object... args) {
        return Resources.getMessage(key, args);
    }

    public static void validateId(Object object, String name, Long id) {
        validateId(object, name, id.toString());
    }

    public static void validateId(Object object, String name, String id) {
        if (object == null) {
            throw new BusinessException(getMessage(VALIDATE_ID, name, id));
        }
    }

    public static void duplicate(Object object, String name) {
        if (object != null) {
            throw new BusinessException(getMessage(DUPLICATE, name));
        }
    }

    public static void notNull(Object object, String name) {
        if (object == null) {
            throw new BusinessException(getMessage(NOT_NULL, name));
        }
    }

    public static void notNull(String str, String name) {
        if (StringUtils.isBlank(str)) {
            throw new BusinessException(getMessage(NOT_NULL, name));
        }
    }

}
