package org.jaguar.commons.basecrud;

import lombok.extern.slf4j.Slf4j;
import org.jaguar.commons.web.JsonResult;
import org.jaguar.commons.web.ResultCode;
import org.jaguar.commons.web.exception.BaseException;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
     * 数据权限异常
     */
    /*@ExceptionHandler(value = {AuthenticationException.class})
    public JsonResult<String> authenticationExceptionHandler(Exception exception) {
        log.error(exception.getMessage());
        return new JsonResult<>(ResultCode.FORBIDDEN, null, exception.getMessage());
    }*/

    /**
     * Controller接口注解RequiresPermissions异常
     */
    /*@ExceptionHandler(value = {UnauthorizedException.class})
    public JsonResult<String> unauthorizedExceptionHandler(Exception exception) {
        String message = exception.getMessage();
        log.error(message);
        String permission = message.substring(message.indexOf('[') + 1, message.length() - 1);
        return new JsonResult<>(ResultCode.FORBIDDEN, null, "对不起，您没有【" + permission + "】操作权限！");
    }*/

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
     * 数据库数据访问异常
     */
    @ResponseBody
    @ExceptionHandler(value = DataAccessException.class)
    public JsonResult<String> dataAccessException(DataAccessException exception) {
        log.error(exception.getMessage(), exception);
        return new JsonResult<>(ResultCode.INTERNAL_SERVER_ERROR, null, exception.getMessage());
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
