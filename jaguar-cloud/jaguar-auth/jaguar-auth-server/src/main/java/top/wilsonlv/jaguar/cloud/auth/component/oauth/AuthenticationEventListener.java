package top.wilsonlv.jaguar.cloud.auth.component.oauth;
import java.time.LocalDateTime;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.stereotype.Component;
import top.wilsonlv.jaguar.cloud.handlerlog.client.HandlerLogConstant;
import top.wilsonlv.jaguar.cloud.handlerlog.client.dto.LoginLogSaveDTO;
import top.wilsonlv.jaguar.commons.enums.ClientType;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityUser;
import top.wilsonlv.jaguar.commons.oauth2.util.SecurityUtil;
import top.wilsonlv.jaguar.commons.web.util.WebUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lvws
 * @since 2021/7/26
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationEventListener {

    private final JmsTemplate jmsQueueTemplate;

    @EventListener
    public void successEvent(AuthenticationSuccessEvent event) {
        Object source = event.getSource();
        if (source instanceof SecurityUser) {
            Authentication authentication = event.getAuthentication();
            log.info("-----------------用户登陆鉴权成功:{}-----------------", authentication.getName());

            SecurityUser securityUser = (SecurityUser) source;

            HttpServletRequest request = WebUtil.getRequest();

            assert request != null;
            String password = request.getParameter("password");

            LoginLogSaveDTO loginLog = new LoginLogSaveDTO();
            loginLog.setPrincipal(authentication.getName());
            loginLog.setCredentials(password);
            loginLog.setPasswordFree(false);
            loginLog.setLoginUserId(securityUser.getId());
            loginLog.setLoginUserType(securityUser.getUserType());
            loginLog.setLoginIp(WebUtil.getHost());
            loginLog.setLoginTime(LocalDateTime.now());
//            loginLog.setAccessToken();
            loginLog.setLoginSuccess(true);

            loginLog.setClientId("");
            loginLog.setClientType(ClientType.PC);
            loginLog.setClientVersion("");
            loginLog.setDeviceModel("");
            loginLog.setDeviceSysVersion("");
            loginLog.setDeviceImei("");
            loginLog.setTenantId("");

            jmsQueueTemplate.convertAndSend(HandlerLogConstant.DESTINATION_LOGIN_LOG, loginLog);
        }
    }

    @EventListener
    public void failureBadCredentialsEvent(AuthenticationFailureBadCredentialsEvent event) {
        log.info("-----------------用户名或者密码错误-----------------");
    }

}
