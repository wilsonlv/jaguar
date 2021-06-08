package org.jaguar.commons.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lvws
 * @since 2021/6/7
 */
@Data
@Configuration
@ConfigurationProperties("jaguar.security")
public class SpringSecurityProperties {

    private String[] ignoreUrls = new String[]{};

}
