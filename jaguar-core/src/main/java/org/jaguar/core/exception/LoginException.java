package org.jaguar.core.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by lvws on 2019/5/23.
 */
public class LoginException extends BaseException {

    public LoginException() {
    }

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginException(Throwable cause) {
        super(cause);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
