package top.wilsonlv.jaguar.cloud.auth.component;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.wilsonlv.jaguar.commons.web.response.JsonResult;
import top.wilsonlv.jaguar.commons.web.response.ResultCode;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author lvws
 * @since 2021/7/3
 */
@Component
@RequiredArgsConstructor
public class CaptchaFilter extends OncePerRequestFilter {

    private final CaptchaService captchaService;

    private final static String SECURITY_LOGIN_PATH = "/login";

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        if (SECURITY_LOGIN_PATH.equals(request.getRequestURI())) {
            checkCaptcha(request, response, filterChain);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void checkCaptcha(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        CaptchaVO captcha = new CaptchaVO();
        captcha.setCaptchaVerification(request.getParameter("captchaVerification"));
        ResponseModel responseModel = captchaService.verification(captcha);

        if (responseModel.isSuccess()) {
            filterChain.doFilter(request, response);
        } else {
            response.setContentType(MediaType.APPLICATION_JSON.toString());
            try (PrintWriter writer = response.getWriter()) {
                JsonResult<Void> result = new JsonResult<>(ResultCode.CONFLICT, null, responseModel.getRepMsg());
                writer.write(result.toJsonStr());
            }
        }
    }
}
