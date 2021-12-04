package top.wilsonlv.jaguar.commons.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import top.wilsonlv.jaguar.commons.web.response.JsonResult;
import top.wilsonlv.jaguar.commons.web.response.ResultCode;

import javax.validation.ConstraintViolationException;

/**
 * @author lvws
 * @since 2021/4/5
 */
@Slf4j
@Configuration
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 请求参数验证错误
     */
    @ResponseBody
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public JsonResult<String> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage());
        StringBuilder errorMessage = new StringBuilder();
        for (ObjectError objectError : exception.getBindingResult().getAllErrors()) {
            errorMessage.append("【").append(objectError.getDefaultMessage()).append("】");
        }

        return new JsonResult<>(ResultCode.BAD_REQUEST, null, errorMessage.toString());
    }

    /**
     * 请求参数错误
     */
    @ResponseBody
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class, ConstraintViolationException.class,
            MissingServletRequestParameterException.class, HttpMediaTypeNotSupportedException.class, HttpMessageNotReadableException.class})
    public JsonResult<String> badRequestExceptionHandler(Exception exception) {
        log.error(exception.getMessage());
        return new JsonResult<>(ResultCode.BAD_REQUEST, null, exception.getMessage());
    }

    /**
     * 请求方式错误
     */
    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public JsonResult<String> httpRequestMethodNotSupportedExceptionHandler(Exception exception) {
        log.error(exception.getMessage());
        return new JsonResult<>(ResultCode.METHOD_NOT_ALLOWED, null, exception.getMessage());
    }

    /**
     * 已定义的基础异常
     */
    @ResponseBody
    @ExceptionHandler(value = BaseException.class)
    public JsonResult<Object> baseExceptionHandler(BaseException exception) {
        log.error(exception.getMessage(), exception);
        return new JsonResult<>(exception.getResultCode(), exception.getData(), exception.getMessage());
    }

    /**
     * 未知的异常捕获处理
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public JsonResult<String> allUnknownExceptionHandler(Exception exception) {
        log.error(exception.getMessage(), exception);
        Throwable e = exception;
        while (e.getCause() != null) {
            e = e.getCause();
        }
        return new JsonResult<>(ResultCode.INTERNAL_SERVER_ERROR, null, e.getMessage());
    }

}
