package top.wilsonlv.jaguar.commons.web.exception.impl;

import top.wilsonlv.jaguar.commons.web.ResultCode;
import top.wilsonlv.jaguar.commons.web.exception.BaseException;

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
