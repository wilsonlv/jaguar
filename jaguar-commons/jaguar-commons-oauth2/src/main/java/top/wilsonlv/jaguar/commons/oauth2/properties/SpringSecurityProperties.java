package top.wilsonlv.jaguar.commons.oauth2.properties;

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

    private String actuatorAdminIp;

    private String[] ignoreUrls;

}
