package org.jaguar.commons.oauth2.component;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jaguar.commons.web.JsonResult;
import org.jaguar.commons.web.ResultCode;
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
        if (e instanceof InsufficientAuthenticationException) {
            //需要登录
            log.info("用户需要登录，访问[{}]失败", request.getRequestURI());

            try (PrintWriter writer = response.getWriter()) {
                JsonResult<?> jsonResult = new JsonResult<>(ResultCode.UNAUTHORIZED, null, e.getMessage());
                writer.write(JSONObject.toJSONString(jsonResult));
            }
            return;
        } else if (e instanceof BadCredentialsException) {
            // 密码错误
            log.error("登录失败 - 用户密码错误");

        } else if (e instanceof AccountExpiredException) {
            // 账号过期
            log.error("登录失败 - 用户账号过期");

        } else if (e instanceof CredentialsExpiredException) {
            // 密码过期
            log.error("登录失败 - 用户密码过期");

        } else if (e instanceof DisabledException) {
            // 用户被禁用
            log.error("登录失败 - 用户被禁用");

        } else if (e instanceof LockedException) {
            // 用户被锁定
            log.error("登录失败 - 用户被锁定");

        } else if (e instanceof InternalAuthenticationServiceException) {
            // 内部错误
            log.error("内部错误 - [{}]", e.getMessage());

        } else {
            // 其他错误
            log.error("其他错误 - [{}]", e.getMessage());
        }

        try (PrintWriter writer = response.getWriter()) {
            JsonResult<?> jsonResult = new JsonResult<>(ResultCode.CONFLICT, null, e.getMessage());
            writer.write(JSONObject.toJSONString(jsonResult));
        }
    }
}
