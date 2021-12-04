package top.wilsonlv.jaguar.oauth2.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

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

    private Map<String, String> scopeUrls;

}
