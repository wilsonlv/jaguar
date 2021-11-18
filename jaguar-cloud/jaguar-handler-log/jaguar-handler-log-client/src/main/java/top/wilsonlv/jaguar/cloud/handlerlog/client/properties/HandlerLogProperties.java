package top.wilsonlv.jaguar.cloud.handlerlog.client.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lvws
 * @since 2021/11/16
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jaguar.handler-log")
public class HandlerLogProperties {

    private Boolean enable = true;

}
