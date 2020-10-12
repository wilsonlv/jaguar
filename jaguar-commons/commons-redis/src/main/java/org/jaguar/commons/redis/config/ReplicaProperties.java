package org.jaguar.commons.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lvws
 * @since 2020/10/10
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis.replica")
public class ReplicaProperties {

    private Boolean enable = false;
    private Set<String> slaves = new HashSet<>();

}
