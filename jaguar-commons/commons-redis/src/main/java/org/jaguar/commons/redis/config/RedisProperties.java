package org.jaguar.commons.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lvws
 * @since  2019/4/22.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jaguar.redis")
public class RedisProperties {

    private Integer database;
    private String host;
    private Integer port;
    private String password;

    private Integer expiration;
    private String namespace;

}
