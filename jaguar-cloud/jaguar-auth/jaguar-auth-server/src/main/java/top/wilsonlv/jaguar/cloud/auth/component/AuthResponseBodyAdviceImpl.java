package top.wilsonlv.jaguar.cloud.auth.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.wilsonlv.jaguar.cloud.handlerlog.client.HandlerLogConstant;
import top.wilsonlv.jaguar.cloud.handlerlog.client.dto.LoginLogSaveDTO;
import top.wilsonlv.jaguar.commons.enums.ClientType;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityUser;
import top.wilsonlv.jaguar.commons.oauth2.util.SecurityUtil;
import top.wilsonlv.jaguar.commons.web.util.WebUtil;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author lvws
 * @since 2021/8/10
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class AuthResponseBodyAdviceImpl implements ResponseBodyAdvice<Object> {

    private final TokenStore tokenStore;

    private final JmsTemplate jmsQueueTemplate;

    private final ClientDetailsService clientDetailsService;

    @Override
    public boolean supports(@Nonnull MethodParameter returnType, @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, @Nonnull MethodParameter returnType, @Nonnull MediaType selectedContentType,
                                  @Nonnull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @Nonnull ServerHttpRequest httpServletRequest, @Nonnull ServerHttpResponse response) {
        if (body instanceof OAuth2AccessToken) {
            loginSuccessHandler(body);
        }
        return body;
    }

    private void loginSuccessHandler(Object body) {
        OAuth2AccessToken oAuth2AccessToken = (OAuth2AccessToken) body;
        OAuth2Authentication authentication = tokenStore.readAuthentication(oAuth2AccessToken.getValue());
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        log.info("-----------------用户登陆鉴权成功:{}-----------------", authentication.getName());

        HttpServletRequest request = WebUtil.getRequest();
        assert request != null;
        String password = request.getParameter("password");
        String clientVersion = request.getParameter("clientVersion");
        String deviceModel = request.getParameter("deviceModel");
        String deviceSysVersion = request.getParameter("deviceSysVersion");
        String deviceImei = request.getParameter("deviceImei");

        LoginLogSaveDTO loginLog = new LoginLogSaveDTO();
        loginLog.setPrincipal(authentication.getName());
        loginLog.setCredentials(password);
        loginLog.setPasswordFree(false);
        loginLog.setLoginUserId(securityUser.getId());
        loginLog.setLoginUserType(securityUser.getUserType());
        loginLog.setLoginIp(WebUtil.getHost());
        loginLog.setLoginTime(LocalDateTime.now());
        loginLog.setLoginSuccess(true);
        loginLog.setAccessToken(oAuth2AccessToken.getValue());

        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(SecurityUtil.getClientId());
        Map<String, Object> additionalInformation = clientDetails.getAdditionalInformation();
        String clientType = (String) additionalInformation.get("clientType");

        loginLog.setClientId(clientDetails.getClientId());
        if (StringUtils.isNotBlank(clientType)) {
            loginLog.setClientType(ClientType.valueOf(clientType));
        }
        loginLog.setClientVersion(clientVersion);
        loginLog.setDeviceModel(deviceModel);
        loginLog.setDeviceSysVersion(deviceSysVersion);
        loginLog.setDeviceImei(deviceImei);
        loginLog.setTenantId(securityUser.getTenantId());

        log.info("send login log");
        try {
            jmsQueueTemplate.convertAndSend(HandlerLogConstant.DESTINATION_LOGIN_LOG, loginLog);
        } catch (JmsException e) {
            log.error(e.getMessage());
        }
    }
}
