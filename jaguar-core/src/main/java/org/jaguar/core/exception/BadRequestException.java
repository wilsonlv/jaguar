package org.jaguar.core.exception;

import org.springframework.http.HttpStatus;

/**
 * @author lvws
 * @since 2020/6/16
 */
public class BadRequestException extends BaseException {

    public BadRequestException(Object data, String message) {
        super(data, message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
