package org.jaguar.commons.swagger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lvws on 2019/4/22.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    private String title = "jaguar-api";
    private String description = "Copyright © 清宁智能";
    private String version = "master";
    private String contactName = "lvws";
    private String contactUrl;
    private String contactEmail;

}
