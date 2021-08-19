package top.wilsonlv.jaguar.commons.feign.exception;

/**
 * @author lvws
 * @since 2021/8/18
 */
public class FeignException extends RuntimeException {

    public FeignException() {
    }

    public FeignException(String message) {
        super(message);
    }

    public FeignException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeignException(Throwable cause) {
        super(cause);
    }

}
