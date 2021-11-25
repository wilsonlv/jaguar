package top.wilsonlv.jaguar.commons.web.exception.impl;

import top.wilsonlv.jaguar.commons.web.ResultCode;
import top.wilsonlv.jaguar.commons.web.exception.BaseException;

/**
 * @author lvws
 * @since 2020/6/16
 */
public class SpecialException extends BaseException {

    public SpecialException(Object data, String message) {
        super(data, message);
    }

    @Override
    public ResultCode getResultCode() {
        return ResultCode.SPECIAL_EXCEPTION;
    }
}
