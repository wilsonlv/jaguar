package org.jaguar.core.exception;

import org.jaguar.core.web.ResultCode;

/**
 * @author lvws
 * @since 2020/6/16
 */
public class BadRequestException extends BaseException {

    public BadRequestException(Object data, String message) {
        super(data, message);
    }

    @Override
    public ResultCode getResultCode() {
        return ResultCode.BAD_REQUEST;
    }
}
