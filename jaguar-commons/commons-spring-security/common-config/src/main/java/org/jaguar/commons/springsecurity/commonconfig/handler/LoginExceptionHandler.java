package org.jaguar.commons.springsecurity.commonconfig.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lvws
 * @since 2021/4/4
 */
@Slf4j
public class LoginExceptionHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        if (e instanceof BadCredentialsException) {
            // 密码错误
            log.info("登录失败 - 用户密码错误");

        } else if (e instanceof AccountExpiredException) {
            // 账号过期
            log.info("登录失败 - 用户账号过期");

        } else if (e instanceof CredentialsExpiredException) {
            // 密码过期
            log.info("登录失败 - 用户密码过期");

        } else if (e instanceof DisabledException) {
            // 用户被禁用
            log.info("登录失败 - 用户被禁用");

        } else if (e instanceof LockedException) {
            // 用户被锁定
            log.info("登录失败 - 用户被锁定");

        } else if (e instanceof InternalAuthenticationServiceException) {
            // 内部错误
            log.info("内部错误 - [%s]", e.getMessage());

        } else {
            // 其他错误
            log.info("其他错误 - [%s]", e.getMessage());
        }

        throw e;
    }
}
