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

    private String cookieName;

    private String loginUrl;

    private String unauthorizedUrl;

    private String filterChainDefinitions;

}
