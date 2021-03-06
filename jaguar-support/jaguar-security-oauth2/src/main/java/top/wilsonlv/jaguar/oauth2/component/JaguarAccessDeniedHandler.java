package top.wilsonlv.jaguar.oauth2.component;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import top.wilsonlv.jaguar.commons.web.response.JsonResult;
import top.wilsonlv.jaguar.commons.web.response.ResultCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author lvws
 * @since 2021/6/29
 */
@Component
public class JaguarAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        JsonResult<Void> result = new JsonResult<>(ResultCode.FORBIDDEN, null, accessDeniedException.getMessage());
        try (PrintWriter writer = response.getWriter()) {
            writer.write(result.toJsonStr());
        }
    }
}
