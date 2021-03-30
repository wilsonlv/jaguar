package org.jaguar.core.exception;

import org.jaguar.core.web.ResultCode;

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
