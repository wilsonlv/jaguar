package org.jaguar.commons.springsecurity.tokenauth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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

}
