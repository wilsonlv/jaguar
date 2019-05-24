package org.jaguar.core.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by lvws on 2019/3/11.
 */
public class AuthenticationException extends BaseException {

    public AuthenticationException() {
    }

    public AuthenticationException(Throwable ex) {
        super(ex);
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable ex) {
        super(message, ex);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
