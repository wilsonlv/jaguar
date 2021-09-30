package top.wilsonlv.jaguar.commons.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author lvws
 * @since 2021/3/30.
 */
public enum ResultCode {

    /**
     * 请求成功
     */
    OK(200, "请求成功"),

    /**
     * 请求参数错误
     */
    BAD_REQUEST(400, "请求参数错误"),

    /**
     * 没有登录
     */
    UNAUTHORIZED(401, "没有登录"),

    /**
     * 没有权限
     */
    FORBIDDEN(403, "没有权限"),

    /**
     * 请求方式错误
     */
    METHOD_NOT_ALLOWED(405, "请求方式错误"),

    /**
     * 检出异常
     */
    CONFLICT(409, "检出异常"),

    /**
     * 请求频繁
     */
    TOO_MANY_REQUESTS(429, "请求频繁"),

    /**
     * 系统内部错误
     */
    INTERNAL_SERVER_ERROR(500, "系统内部错误"),

    /**
     * 网关错误
     */
    BAD_GATEWAY(502, "网关错误"),

    /**
     * 特殊业务异常
     */
    SPECIAL_EXCEPTION(1000, "特殊业务异常"),

    /**
     * 增删改查错误
     */
    DATA_CRUD_EXCEPTION(1001, "增删改查错误");

    private final int value;
    private final String reasonPhrase;

    ResultCode(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    @JsonCreator
    public static ResultCode forValue(int value) {
        for (ResultCode resultCode : ResultCode.values()) {
            if (resultCode.getValue() == value) {
                return resultCode;
            }
        }
        return null;
    }

}
