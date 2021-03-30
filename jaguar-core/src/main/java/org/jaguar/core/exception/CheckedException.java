package org.jaguar.core.exception;

import org.jaguar.core.web.ResultCode;

/**
 * @author lvws
 * @since 2019/5/23.
 */
public class CheckedException extends BaseException {

    public CheckedException(String message) {
        super(message);
    }

    public CheckedException(Throwable cause) {
        super(cause);
    }

    @Override
    public ResultCode getResultCode() {
        return ResultCode.CONFLICT;
    }
}
