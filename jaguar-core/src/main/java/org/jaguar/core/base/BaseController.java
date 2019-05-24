/**
 *
 */
package org.jaguar.core.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jaguar.core.exception.BaseException;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.LoginUtil;
import org.jaguar.core.web.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 控制器基类
 *
 * @author lvws
 * @version 2016年5月20日 下午3:47:58
 */
public abstract class BaseController {

    protected final Logger logger = LogManager.getLogger(this.getClass());

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


    protected ResponseEntity<JsonResult> success() {
        return ResponseEntity.ok(new JsonResult());
    }

    protected <T> ResponseEntity<JsonResult<T>> success(T data) {
        return ResponseEntity.ok(new JsonResult<T>(data));
    }

    protected <T> ResponseEntity<JsonResult<Page<T>>> success(IPage<T> data) {
        Page<T> page = Page.convert(data);
        return ResponseEntity.ok(new JsonResult<>(page));
    }

    /**
     * 请求参数错误
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<JsonResult<String>> ConstraintViolationExceptionHandler(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new JsonResult<String>().setMessage(exception.getMessage()));
    }

    /**
     * 请求方式错误
     */
    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<JsonResult<String>> httpRequestMethodNotSupportedExceptionHandler(Exception exception) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new JsonResult<String>().setMessage(exception.getMessage()));
    }

    /**
     * 已定义的基础异常
     */
    @ResponseBody
    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<JsonResult<String>> baseExceptionHandler(BaseException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(new JsonResult<String>().setMessage(exception.getMessage()));
    }

    /**
     * 未知的异常捕获处理
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<JsonResult<String>> allUnknownExceptionHandler(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new JsonResult<String>().setMessage(exception.getMessage()));
    }

}
