package top.wilsonlv.jaguar.cloud.auth.component.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Component;
import top.wilsonlv.jaguar.cloud.handlerlog.client.HandlerLogConstant;
import top.wilsonlv.jaguar.cloud.handlerlog.client.dto.LoginLogSaveDTO;
import top.wilsonlv.jaguar.commons.enums.ClientType;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityUser;
import top.wilsonlv.jaguar.commons.oauth2.util.SecurityUtil;
import top.wilsonlv.jaguar.commons.web.util.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author lvws
 * @since 2021/7/26
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationEventListener {

    private final JmsTemplate jmsQueueTemplate;

    private final ClientDetailsService clientDetailsService;

    @EventListener
    public void successEvent(AuthenticationSuccessEvent event) {
        Object source = event.getSource();
        if (source instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) source;
            if(token.getPrincipal() instanceof SecurityUser){
                SecurityUser securityUser = (SecurityUser) token.getPrincipal();
                log.info("-----------------用户登陆鉴权成功:{}-----------------", token.getName());

                HttpServletRequest request = WebUtil.getRequest();

                assert request != null;
                String password = request.getParameter("password");
                String clientVersion = request.getParameter("clientVersion");
                String deviceModel = request.getParameter("deviceModel");
                String deviceSysVersion = request.getParameter("deviceSysVersion");
                String deviceImei = request.getParameter("deviceImei");

                LoginLogSaveDTO loginLog = new LoginLogSaveDTO();
                loginLog.setPrincipal(token.getName());
                loginLog.setCredentials(password);
                loginLog.setPasswordFree(false);
                loginLog.setLoginUserId(securityUser.getId());
                loginLog.setLoginUserType(securityUser.getUserType());
                loginLog.setLoginIp(WebUtil.getHost());
                loginLog.setLoginTime(LocalDateTime.now());
                loginLog.setLoginSuccess(true);
//            loginLog.setAccessToken();

                ClientDetails clientDetails = clientDetailsService.loadClientByClientId(SecurityUtil.getClientId());
                Map<String, Object> additionalInformation = clientDetails.getAdditionalInformation();
                String clientType = (String) additionalInformation.get("clientType");

                loginLog.setClientId(clientDetails.getClientId());
                if(StringUtils.isNotBlank(clientType)){
                    loginLog.setClientType(ClientType.valueOf(clientType));
                }
                loginLog.setClientVersion(clientVersion);
                loginLog.setDeviceModel(deviceModel);
                loginLog.setDeviceSysVersion(deviceSysVersion);
                loginLog.setDeviceImei(deviceImei);
                loginLog.setTenantId(securityUser.getTenantId());

                log.info("send login log");
                jmsQueueTemplate.convertAndSend(HandlerLogConstant.DESTINATION_LOGIN_LOG, loginLog);
            }
        }
    }

    @EventListener
    public void failureBadCredentialsEvent(AuthenticationFailureBadCredentialsEvent event) {
        log.info("-----------------用户名或者密码错误-----------------");
    }

}
