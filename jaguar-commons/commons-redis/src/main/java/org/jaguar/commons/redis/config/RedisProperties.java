package org.jaguar.commons.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lvws on 2019/4/22.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {

    private Integer database = 0;
    private String host = "127.0.0.1";
    private Integer port = 6379;
    private String password;

    private Integer expiration = 604800;
    private String namespace = "jaguar";

}
