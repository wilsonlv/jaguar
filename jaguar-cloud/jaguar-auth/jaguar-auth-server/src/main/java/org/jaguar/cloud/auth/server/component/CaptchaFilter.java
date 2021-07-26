package org.jaguar.cloud.auth.server.component;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jaguar.commons.oauth2.component.RedisClientDetailsServiceImpl;
import org.jaguar.commons.web.JsonResult;
import org.jaguar.commons.web.ResultCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author lvws
 * @since 2021/7/3
 */
@Component
@RequiredArgsConstructor
public class CaptchaFilter extends OncePerRequestFilter {

    private final CaptchaService captchaService;

    private final RedisClientDetailsServiceImpl clientDetailsService;

    private final static String OAUTH_TOKEN_PATH = "/oauth/token";

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        if (!OAUTH_TOKEN_PATH.equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(authentication.getName());
        Map<String, Object> additionalInformation = clientDetails.getAdditionalInformation();
        Boolean isCaptcha = (Boolean) additionalInformation.get("captcha");
        if (isCaptcha != null && !isCaptcha) {
            filterChain.doFilter(request, response);
            return;
        }

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
