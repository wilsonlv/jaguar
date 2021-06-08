package org.jaguar.commons.web.exception;

import org.apache.commons.lang3.StringUtils;
import org.jaguar.commons.web.ResultCode;


/**
 * @author lvws
 * @since 2019/5/23.
 */
public abstract class BaseException extends RuntimeException {

    protected Object data;

    public BaseException() {
    }

    public BaseException(Object data, String message) {
        super(message);
        this.data = data;
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

    /**
     * 异常对应的的状态码
     *
     * @return 状态码枚举类
     */
    public abstract ResultCode getResultCode();

    @Override
    public String getMessage() {
        String message = super.getMessage();
        return StringUtils.isNotBlank(message) ? message : getResultCode().getReasonPhrase();
    }

    public Object getData() {
        return data;
    }
}
