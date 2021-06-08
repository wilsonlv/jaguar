package org.jaguar.commons.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lvws
 * @since 2021/6/7
 */
@Data
@ConfigurationProperties("jaguar.security")
public class SpringSecurityProperties {

    private String[] ignoreUrls;

}
