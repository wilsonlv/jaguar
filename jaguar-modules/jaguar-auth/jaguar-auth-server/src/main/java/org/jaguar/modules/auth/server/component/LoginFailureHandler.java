package org.jaguar.modules.auth.server.component;

import lombok.extern.slf4j.Slf4j;
import org.jaguar.commons.web.JsonResult;
import org.jaguar.commons.web.ResultCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author lvws
 * @since 2021/6/8
 */
@Slf4j
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        JsonResult<Void> result = new JsonResult<>(ResultCode.CONFLICT, null, exception.getMessage());
        try (PrintWriter writer = response.getWriter()) {
            writer.write(result.toJsonStr());
        }
    }
}
