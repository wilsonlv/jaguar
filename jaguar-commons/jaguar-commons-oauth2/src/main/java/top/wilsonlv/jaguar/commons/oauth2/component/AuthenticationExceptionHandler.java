package top.wilsonlv.jaguar.commons.oauth2.component;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import top.wilsonlv.jaguar.commons.web.JsonResult;
import top.wilsonlv.jaguar.commons.web.ResultCode;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author lvws
 * @since 2021/4/4
 */
@Slf4j
@Component
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {

        ResultCode resultCode = ResultCode.CONFLICT;
        String message = e.getMessage();

        if (e instanceof InsufficientAuthenticationException) {
            log.info("用户需要登录，访问[{}]失败", request.getRequestURI());
            resultCode = ResultCode.UNAUTHORIZED;
        } else if (e instanceof BadCredentialsException) {
            log.error("登录失败 - 用户密码错误");
        } else if (e instanceof AccountExpiredException) {
            log.error("登录失败 - 用户账号过期");
        } else if (e instanceof CredentialsExpiredException) {
            log.error("登录失败 - 用户密码过期");
        } else if (e instanceof DisabledException) {
            log.error("登录失败 - 用户被禁用");
        } else if (e instanceof LockedException) {
            log.error("登录失败 - 用户被锁定");
        } else if (e instanceof InternalAuthenticationServiceException) {
            if (e.getCause() != null) {
                message = e.getCause().getMessage();
                log.error("其他错误 - [{}]", message);
            } else {
                log.error("内部错误 - [{}]", message);
            }
        } else {
            // 其他错误
            log.error("其他错误 - [{}]", message);
        }

        try (PrintWriter writer = response.getWriter()) {
            JsonResult<?> jsonResult = new JsonResult<>(resultCode, null, message);
            writer.write(JSONObject.toJSONString(jsonResult));
        }
    }
}
