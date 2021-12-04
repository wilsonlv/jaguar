package top.wilsonlv.jaguar.cloud.auth.component.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.wilsonlv.jaguar.commons.web.response.JsonResult;
import top.wilsonlv.jaguar.commons.web.response.ResultCode;

import java.util.Map;

/**
 * @author lvws
 * @since 2021/11/17
 */
@Slf4j
@Configuration
@RestControllerAdvice
public class Oauth2ExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {OAuth2Exception.class})
    public JsonResult<Map<String, String>> oauth2Exception(OAuth2Exception exception) {
        log.error(exception.getMessage(), exception);
        return new JsonResult<>(ResultCode.CONFLICT, exception.getAdditionalInformation(), exception.getMessage());
    }

}
