package top.wilsonlv.jaguar.cloud.services;

import lombok.Getter;

/**
 * @author lvws
 * @since 2021/11/11
 */
public enum JaguarSever {

    UPMS(JaguarServerName.JAGUAR_UPMS_SERVER, "用户权限管理系统"),

    AUTH(JaguarServerName.JAGUAR_AUTH_SERVER, "认证服务器");

    @Getter
    private final String name;

    @Getter
    private final String alias;

    JaguarSever(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }
}
