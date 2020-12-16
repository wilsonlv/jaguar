package org.jaguar.commons.shiro.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lvws
 * @since 2019/4/18.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jaguar.shiro")
public class ShiroProperties {

    private String cookieName;

    private String loginUrl;

    private String unauthorizedUrl;

    private String filterChainDefinitions;

    private Boolean authcEnable;
}
