package com.itqingning.core.exception;

import com.itqingning.core.http.HttpCode;

/**
 * @author lvws
 * @version 2016年5月20日 下午3:19:19
 */
@SuppressWarnings("serial")
public class BusinessException extends BaseException {
    public BusinessException() {
    }

    public BusinessException(Throwable ex) {
        super(ex);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable ex) {
        super(message, ex);
    }

    public HttpCode getHttpCode() {
        return HttpCode.CONFLICT;
    }
}