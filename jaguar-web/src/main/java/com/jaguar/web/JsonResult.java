package com.jaguar.web;

import com.jaguar.core.http.HttpCode;
import org.springframework.core.NamedThreadLocal;
import org.springframework.ui.ModelMap;

/**
 * Created by lvws on 2019/4/16.
 */
public class JsonResult extends ModelMap {

    public static final String JR_DATA = "data";
    public static final String JR_HTTP_CODE = "httpCode";
    public static final String JR_MSG = "msg";
    public static final String JR_TIME_STAMP = "timestamp";
    public static final String JR_CURRENT = "current";
    public static final String JR_SIZE = "size";
    public static final String JR_PAGES = "pages";
    public static final String JR_TOTAL = "total";

    public static final ThreadLocal<HttpCode> HTTP_CODE = new NamedThreadLocal<>("HTTP_CODE");


    public Object getData() {
        return this.get(JR_DATA);
    }

    public void setData(Object data) {
        this.put(JR_DATA, data);
    }

    public Integer getHttpCode() {
        return (Integer) this.get(JR_HTTP_CODE);
    }

    public void setHttpCode(HttpCode httpCode) {
        this.put(JR_HTTP_CODE, httpCode.value());
        HTTP_CODE.set(httpCode);
    }

    public String getMsg() {
        return (String) this.get(JR_MSG);
    }

    public void setMsg(String msg) {
        this.put(JR_MSG, msg);
    }

    public Long getTimestamp() {
        return (Long) this.get(JR_TIME_STAMP);
    }

    public void setTimestamp(Long timestamp) {
        this.put(JR_TIME_STAMP, timestamp);
    }

    public Integer getCurrent() {
        return (Integer) this.get(JR_CURRENT);
    }

    public void setCurrent(Long current) {
        this.put(JR_CURRENT, current.intValue());
    }

    public Integer getSize() {
        return (Integer) this.get(JR_SIZE);
    }

    public void setSize(Long size) {
        this.put(JR_SIZE, size.intValue());
    }

    public Integer getPages() {
        return (Integer) this.get(JR_PAGES);
    }

    public void setPages(Long pages) {
        this.put(JR_PAGES, pages.intValue());
    }

    public Integer getTotal() {
        return (Integer) this.get(JR_TOTAL);
    }

    public void setTotal(Long total) {
        this.put(JR_TOTAL, total.intValue());
    }
}
