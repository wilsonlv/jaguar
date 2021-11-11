package top.wilsonlv.jaguar.commons.feign.component;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.wilsonlv.jaguar.commons.web.JsonResult;
import top.wilsonlv.jaguar.commons.web.ResultCode;

/**
 * @author lvws
 * @since 2021/4/5
 */
@Slf4j
@Configuration
@RestControllerAdvice
public class FeignExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = FeignException.class)
    public JsonResult<String> feignExceptionHandler(FeignException exception) {
        return new JsonResult<>(ResultCode.BAD_GATEWAY, null, exception.getMessage());
    }

}
