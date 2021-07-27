package top.wilsonlv.jaguar.cloud.auth.component;

import lombok.extern.slf4j.Slf4j;
import top.wilsonlv.jaguar.commons.web.JsonResult;
import top.wilsonlv.jaguar.commons.web.ResultCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

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
                                        AuthenticationException exception) throws IOException {
        log.debug("用户登录失败：{}", exception.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        JsonResult<Void> result = new JsonResult<>(ResultCode.CONFLICT, null, exception.getMessage());
        try (PrintWriter writer = response.getWriter()) {
            writer.write(result.toJsonStr());
        }
    }
}
