package org.jaguar.commons.shiro.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lvws on 2019/4/22.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "shiro")
public class ShiroProperties {

    private String cookieName = "JAGUAR-JSESSIONID";

    private String loginUrl = "/unauthorized";

    private String unauthorizedUrl = "/forbidden";

    private String filterChainDefinitions = "/index.jsp," +
            "/*.ico," +
            "/unauthorized," +
            "/forbidden," +
            "/swagger**," +
            "/swagger-resources/**," +
            "/webjars/**," +
            "/*/api-docs," +
            "/login," +
            "/pic_verification_code," +
            "/test," +
            "/test/**,";

}
