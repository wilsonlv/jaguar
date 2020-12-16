package org.jaguar.commons.shiro.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lvws
 * @since 2020/11/28
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jaguar.shiro.redis")
public class ShiroRedisProperties {

    private Boolean sessionEnable;
    private Boolean replicaEnable;

    private String masterHost;
    private Integer masterPort;
    private String masterPassword;
    private Set<String> slaves = new HashSet<>();

}
