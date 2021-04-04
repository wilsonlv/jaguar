package org.jaguar.commons.springsecurity.sessionauth.config;

import lombok.Data;
import org.jaguar.commons.springsecurity.commonconfig.handler.LoginExceptionHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * @author lvws
 * @since 2021/4/4
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jaguar.spring.security.session")
public class SecuritySessionProperties {


    /**
     * 认证成功后的处理事件
     */
    private Class<? extends AuthenticationSuccessHandler> successHandler;

    /**
     * 认证失败后的处理事件
     */
    private Class<? extends AuthenticationFailureHandler> failureHandler = LoginExceptionHandler.class;

}
