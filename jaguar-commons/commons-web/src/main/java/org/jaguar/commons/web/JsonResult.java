package org.jaguar.commons.web;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2019/4/16.
 */
@Data
public class JsonResult<T> implements Serializable {

    public static final String SUCCESS_MSG = "成功";

    private ResultCode resultCode;
    private T data;
    private String message;
    private final Long timestamp = System.currentTimeMillis();

    public JsonResult(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public JsonResult(ResultCode resultCode, T data, String message) {
        this.resultCode = resultCode;
        this.data = data;
        this.message = message;
    }

    public JsonResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public JsonResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

}
