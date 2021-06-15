package org.jaguar.commons.web.exception.impl;

import org.jaguar.commons.web.ResultCode;
import org.jaguar.commons.web.exception.BaseException;

/**
 * @author lvws
 * @since 2021/3/30.
 */
public class DataCrudException extends BaseException {

    public DataCrudException() {
    }

    public DataCrudException(String message) {
        super(message);
    }

    public DataCrudException(Object data, String message) {
        super(data, message);
    }

    @Override
    public ResultCode getResultCode() {
        return ResultCode.DATA_CRUD_EXCEPTION;
    }
}
