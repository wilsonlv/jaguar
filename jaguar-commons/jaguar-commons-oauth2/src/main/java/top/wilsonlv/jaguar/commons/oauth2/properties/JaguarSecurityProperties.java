package top.wilsonlv.jaguar.commons.oauth2.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author lvws
 * @since 2021/6/7
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties("jaguar.security")
public class JaguarSecurityProperties {

    private String serverId;

    private String serverSecret;

    private String[] ignoreUrls;

}
