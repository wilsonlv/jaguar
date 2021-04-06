package org.jaguar.commons.web.exception;

import org.jaguar.commons.web.ResultCode;

/**
 * @author lvws
 * @since 2020/6/17
 */
public class TooManyRequestsException extends BaseException {

    public TooManyRequestsException() {
    }

    public TooManyRequestsException(String message) {
        super(message);
    }

    @Override
    public ResultCode getResultCode() {
        return ResultCode.TOO_MANY_REQUESTS;
    }
}
