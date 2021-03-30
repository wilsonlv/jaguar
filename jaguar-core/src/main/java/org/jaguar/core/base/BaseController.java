package org.jaguar.core.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.jaguar.core.exception.BaseException;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.LoginUtil;
import org.jaguar.core.web.Page;
import org.jaguar.core.web.ResultCode;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

/**
 * 控制器基类
 *
 * @author lvws
 * @version 2016年5月20日 下午3:47:58
 */
@Slf4j
public abstract class BaseController {

    /**
     * 获取当前用户Id
     */
    protected Long getCurrentUser() {
        return LoginUtil.getCurrentUser();
    }

    /**
     * 获取当前用户账号
     */
    protected String getCurrentUserAccount() {
        return LoginUtil.getCurrentUserAccount();
    }


    protected JsonResult<?> success() {
        return new JsonResult<>(ResultCode.OK, null, JsonResult.SUCCESS_MSG);
    }

    protected <T> JsonResult<T> success(T data) {
        return new JsonResult<>(ResultCode.OK, data, JsonResult.SUCCESS_MSG);
    }

    protected <T> JsonResult<Page<T>> success(IPage<T> data) {
        Page<T> page = Page.convert(data);
        return new JsonResult<>(ResultCode.OK, page, JsonResult.SUCCESS_MSG);
    }

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
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class, ConstraintViolationException.class, MissingServletRequestParameterException.class})
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
    @ResponseBody
    @ExceptionHandler(value = {AuthenticationException.class})
    public JsonResult<String> authenticationExceptionHandler(Exception exception) {
        log.error(exception.getMessage());
        return new JsonResult<>(ResultCode.FORBIDDEN, null, exception.getMessage());
    }

    /**
     * Controller接口注解RequiresPermissions异常
     */
    @ResponseBody
    @ExceptionHandler(value = {UnauthorizedException.class})
    public JsonResult<String> unauthorizedExceptionHandler(Exception exception) {
        String message = exception.getMessage();
        log.error(message);
        String permission = message.substring(message.indexOf('[') + 1, message.length() - 1);
        return new JsonResult<>(ResultCode.FORBIDDEN, null, "对不起，您没有【" + permission + "】操作权限！");
    }

    /**
     * 已定义的基础异常
     */
    @ResponseBody
    @ExceptionHandler(value = BaseException.class)
    public JsonResult<Object> baseExceptionHandler(BaseException exception) {
        log.error(exception.getMessage());
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
