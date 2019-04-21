package com.itqingning.jaguar.core.exception;

import com.itqingning.jaguar.core.http.HttpCode;

/**
 * Created by lvws on 2019/2/21.
 */
public class LoginException extends BaseException {

    public LoginException() {
    }

    public LoginException(Throwable ex) {
        super(ex);
    }

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable ex) {
        super(message, ex);
    }

    @Override
    public HttpCode getHttpCode() {
        return HttpCode.LOGIN_FAIL;
    }

}
