package top.wilsonlv.jaguar.cloud.auth.component.security;

import lombok.extern.slf4j.Slf4j;
import top.wilsonlv.jaguar.commons.web.JsonResult;
import top.wilsonlv.jaguar.commons.web.ResultCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.debug("用户[{}]登录成功", authentication.getName());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        JsonResult<Object> jsonResult = new JsonResult<>(ResultCode.OK, authentication.getPrincipal(), "登录成功");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(jsonResult.toJsonStr());
        }
    }
}
