package top.wilsonlv.jaguar.cloud.auth.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import top.wilsonlv.jaguar.commons.web.response.JsonResult;
import top.wilsonlv.jaguar.commons.web.response.ResultCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author lvws
 * @since 2021/12/17
 */
@Slf4j
@Component
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication != null) {
            log.debug("用户[{}]退出成功", authentication.getName());
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        JsonResult<Object> jsonResult = new JsonResult<>(ResultCode.OK, null, "退出成功");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(jsonResult.toJsonStr());
        }
    }
}
