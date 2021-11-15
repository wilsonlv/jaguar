package top.wilsonlv.jaguar.commons.enums;

import lombok.Getter;

/**
 * @author lvws
 * @since 2021/7/26
 */
public enum UserType {

    /**
     * 平台管理员
     */
    ADMIN("平台管理员"),
    /**
     * 平台租户
     */
    TENANT("平台租户"),
    /**
     * 平台用户
     */
    USER("平台用户");

    @Getter
    private final String userTypeName;

    UserType(String userTypeName) {
        this.userTypeName = userTypeName;
    }
}
