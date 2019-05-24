package org.jaguar.core.web;

import java.io.Serializable;

/**
 * Created by lvws on 2019/4/16.
 */
public class JsonResult<T> implements Serializable {


    private T data;
    private String message;

    public JsonResult(){
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
}
