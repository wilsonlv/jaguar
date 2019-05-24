package org.jaguar.core.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

/**
 * Created by lvws on 2019/5/23.
 */
public abstract class BaseException extends RuntimeException {

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public abstract HttpStatus getHttpStatus();

    @Override
    public String getMessage() {
        return StringUtils.isBlank(super.getMessage()) ? getHttpStatus().getReasonPhrase() : super.getMessage();
    }

}
