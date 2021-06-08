package org.jaguar.commons.web;

/**
 * @author lvws
 * @since 2021/3/30.
 */
public enum ResultCode {

    /**
     * 请求成功
     */
    OK(200, "OK"),

    /**
     * 请求参数错误
     */
    BAD_REQUEST(400, "Bad Request"),

    /**
     * 没有登录
     */
    UNAUTHORIZED(401, "Unauthorized"),

    /**
     * 没有权限
     */
    FORBIDDEN(403, "Forbidden"),

    /**
     * 请求方式错误
     */
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    /**
     * 检出异常
     */
    CONFLICT(409, "Conflict"),

    /**
     * 请求频繁
     */
    TOO_MANY_REQUESTS(429, "Too Many Requests"),

    /**
     * 内部错误
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    /**
     * 网关错误
     */
    BAD_GATEWAY(502, "Bad Gateway"),

    /**
     * 特殊业务异常
     */
    SPECIAL_EXCEPTION(1000, "Special Exception"),

    /**
     * 增删改查错误
     */
    DATA_CRUD_EXCEPTION(1001, "Data Crud Exception");

    private final int value;
    private final String reasonPhrase;

    ResultCode(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int getValue() {
        return value;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }
}
