package org.jaguar.cloud.auth.server.component;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jaguar.commons.web.JsonResult;
import org.jaguar.commons.web.ResultCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author lvws
 * @since 2021/7/3
 */
@Component
@RequiredArgsConstructor
public class CaptchaFilter extends OncePerRequestFilter {

    private final CaptchaService captchaService;

    private final static String OAUTH_TOKEN_PATH = "/oauth/token";

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        if (!OAUTH_TOKEN_PATH.equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        CaptchaVO captcha = new CaptchaVO();
        captcha.setCaptchaVerification(request.getParameter("captchaVerification"));
        ResponseModel responseModel = captchaService.verification(captcha);

        if (responseModel.isSuccess()) {
            filterChain.doFilter(request, response);
        } else {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON.toString());

            try (PrintWriter writer = response.getWriter()) {
                JsonResult<ResponseModel> result = new JsonResult<>(ResultCode.CONFLICT, responseModel, responseModel.getRepMsg());
                writer.write(result.toJsonStr());
            }
        }
    }
}
