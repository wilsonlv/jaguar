package org.jaguar.commons.web.exception.impl;

import org.jaguar.commons.web.ResultCode;
import org.jaguar.commons.web.exception.BaseException;

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
        return ResultCode.BAD_REQUEST;
    }
}
