package org.jaguar.core.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.jaguar.core.exception.BaseException;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.LoginUtil;
import org.jaguar.core.web.Page;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    protected ResponseEntity<JsonResult<?>> success() {
        return ResponseEntity.ok(new JsonResult<>().setMessage(JsonResult.SUCCESS_MSG));
    }

    protected <T> ResponseEntity<JsonResult<T>> success(T data) {
        return ResponseEntity.ok(new JsonResult<>(data).setMessage(JsonResult.SUCCESS_MSG));
    }

    protected <T> ResponseEntity<JsonResult<Page<T>>> success(IPage<T> data) {
        Page<T> page = Page.convert(data);
        return ResponseEntity.ok(new JsonResult<>(page).setMessage(JsonResult.SUCCESS_MSG));
    }

    /**
     * 请求参数验证错误
     */
    @ResponseBody
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<JsonResult<String>> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage());
        StringBuilder errorMessage = new StringBuilder();
        for (ObjectError objectError : exception.getBindingResult().getAllErrors()) {
            errorMessage.append("【").append(objectError.getDefaultMessage()).append("】");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new JsonResult<String>().setMessage(errorMessage.toString()));
    }

    /**
     * 请求参数错误
     */
    @ResponseBody
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class, ConstraintViolationException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<JsonResult<String>> badRequestExceptionHandler(Exception exception) {
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new JsonResult<String>().setMessage(exception.getMessage()));
    }

    /**
     * 请求方式错误
     */
    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<JsonResult<String>> httpRequestMethodNotSupportedExceptionHandler(Exception exception) {
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new JsonResult<String>().setMessage(exception.getMessage()));
    }

    /**
     * 数据权限异常
     */
    @ResponseBody
    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<JsonResult<String>> authenticationExceptionHandler(Exception exception) {
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new JsonResult<String>().setMessage(exception.getMessage()));
    }

    /**
     * Controller接口注解RequiresPermissions异常
     */
    @ResponseBody
    @ExceptionHandler(value = {UnauthorizedException.class})
    public ResponseEntity<JsonResult<String>> unauthorizedExceptionHandler(Exception exception) {
        String message = exception.getMessage();
        log.error(message);
        String permission = message.substring(message.indexOf('[') + 1, message.length() - 1);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new JsonResult<String>().setMessage("对不起，您没有【" + permission + "】操作权限！"));
    }

    /**
     * 已定义的基础异常
     */
    @ResponseBody
    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<JsonResult<Object>> baseExceptionHandler(BaseException exception) {
        log.error(exception.getMessage());
        log.error(exception.getStackTrace()[0].toString());
        return ResponseEntity.status(exception.getHttpStatus())
                .body(new JsonResult<>().setData(exception.getData()).setMessage(exception.getMessage()));
    }

    /**
     * 数据库数据访问异常
     */
    @ResponseBody
    @ExceptionHandler(value = DataAccessException.class)
    public ResponseEntity<JsonResult<String>> dataAccessException(DataAccessException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new JsonResult<String>().setMessage(exception.getMessage()));
    }

    /**
     * 未知的异常捕获处理
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<JsonResult<String>> allUnknownExceptionHandler(Exception exception) {
        log.error(exception.getMessage(), exception);
        Throwable e = exception;
        while (e.getCause() != null) {
            e = e.getCause();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new JsonResult<String>().setMessage(e.getMessage()));
    }

}
