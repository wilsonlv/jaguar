package org.jaguar.commons.rediscache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2021/6/24
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jaguar.redis.cache")
public class RedisCacheProperties implements Serializable {

    /**
     * 有效期(d)
     */
    private Integer expire = 7;

}
