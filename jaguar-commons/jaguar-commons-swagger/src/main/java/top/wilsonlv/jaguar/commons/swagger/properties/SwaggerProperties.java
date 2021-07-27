package top.wilsonlv.jaguar.commons.swagger.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lws
 * @since 2019/4/22.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jaguar.swagger")
public class SwaggerProperties {

    private String title;
    private String description;
    private String version;
    private String contactName;
    private String contactUrl;
    private String contactEmail;

}
