package org.jaguar.cloud.auth.server.component.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * @author lvws
 * @since 2021/7/26
 */
@Slf4j
@Component
public class AuthenticationEventListener {

    @EventListener
    public void successEvent(AuthenticationSuccessEvent event) {
        log.info("-----------------用户登陆鉴权成功-----------------");
        // 这里的事件源除了登录事件（UsernamePasswordAuthenticationToken）及token验证事件源（OAuth2Authentication）都会监听到
        /*if(!event.getSource().getClass().getName().equals(
                "org.springframework.security.authentication.UsernamePasswordAuthenticationToken")){
            return ;
        }*/
        /*if(event.getAuthentication().getDetails() != null){
            // TODO 成功日志
        }*/
        log.info(event.getAuthentication().getName());
    }

    @EventListener
    public void failureBadCredentialsEvent(AuthenticationFailureBadCredentialsEvent event) {
        log.info("-----------------用户名或者密码错误-----------------");
        //TODO 失败日志
    }

}
