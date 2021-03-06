package top.wilsonlv.jaguar.basecrud;

import org.springframework.util.StringUtils;
import top.wilsonlv.jaguar.commons.web.base.BaseModifyDTO;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;

import java.math.BigDecimal;

/**
 * @author lvws
 * @since 2019年2月27日
 */
public final class Assert {

    private static final String VALIDATE_ID = "无效的%sID【%s】";
    private static final String VALIDATE_PROPERTY = "无效的%s【%s】";
    private static final String NOT_NULL = "【%s】为非空！";
    private static final String DUPLICATE = "重复的【%s】";

    private static String getMessage(String key, Object... args) {
        return String.format(key, args);
    }

    public static void validateId(Object object, String name, Long id) {
        if (object == null) {
            throw new CheckedException(getMessage(VALIDATE_ID, StringUtils.hasText(name) ? name : "", id));
        }
    }

    public static void validateProperty(Object object, String name, String value) {
        if (object == null) {
            throw new CheckedException(getMessage(VALIDATE_PROPERTY, name, value));
        }
    }

    public static void duplicate(BaseModel unique, String name) {
        if (unique != null) {
            throw new CheckedException(getMessage(DUPLICATE, name));
        }
    }

    public static void duplicate(BaseModel unique, BaseModel update, String name) {
        if (unique != null && !unique.getId().equals(update.getId())) {
            throw new CheckedException(getMessage(DUPLICATE, name));
        }
    }

    public static void duplicate(BaseModel unique, BaseModifyDTO update, String name) {
        if (unique != null && !unique.getId().equals(update.getId())) {
            throw new CheckedException(getMessage(DUPLICATE, name));
        }
    }

    public static void notNull(Object object, String name) {
        if (object == null) {
            throw new CheckedException(getMessage(NOT_NULL, name));
        }
    }

    public static void notNull(String str, String name) {
        if (!StringUtils.hasText(str)) {
            throw new CheckedException(getMessage(NOT_NULL, name));
        }
    }

    public static void isPositive(String num) {
        if (new BigDecimal(num).compareTo(BigDecimal.ZERO) > 0) {
            throw new CheckedException("num '" + num + "' must be positive");
        }
    }

}
