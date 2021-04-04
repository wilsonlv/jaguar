package org.jaguar.commons.springsecurity.tokenauth.config;

import lombok.Data;
import org.jaguar.commons.springsecurity.commonconfig.handler.LoginExceptionHandler;
import org.jaguar.commons.springsecurity.tokenauth.handler.TokenLoginSuccessHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * @author lvws
 * @since 2021/4/4
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jaguar.spring.security.token")
public class SecurityTokenProperties {

    /**
     * rsa私钥
     */
    private String rsaPrivateKey;

    /**
     * rsa公钥
     */
    private String rsaPublicKey;

    /**
     * token有效时长（s）
     */
    private Long timeout = 3600L;

    /**
     * 认证成功后的处理事件
     */
    private Class<? extends TokenLoginSuccessHandler> successHandler = TokenLoginSuccessHandler.class;

    /**
     * 认证失败后的处理事件
     */
    private Class<? extends AuthenticationFailureHandler> failureHandler = LoginExceptionHandler.class;

}
