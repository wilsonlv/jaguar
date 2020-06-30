package org.jaguar.core.exception;

import org.springframework.http.HttpStatus;

/**
 * @author lvws
 * @since 2019/5/23.
 */
public class CheckedException extends BaseException {

    public CheckedException() {
    }

    public CheckedException(String message) {
        super(message);
    }

    public CheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckedException(Throwable cause) {
        super(cause);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}
