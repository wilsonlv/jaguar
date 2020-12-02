package org.jaguar.core.web;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2019/4/16.
 */
public class JsonResult<T> implements Serializable {

    public static final String SUCCESS_MSG = "成功";

    private T data;
    private String message;
    private final Long timestamp = System.currentTimeMillis();

    public JsonResult() {
    }

    public JsonResult(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public JsonResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public JsonResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
