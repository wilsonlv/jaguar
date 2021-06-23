package org.jaguar.modules.codegen.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.Map;

/**
 * @author lvws
 * @since 2021/6/19
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "jaguar.modules.code")
public class CodegenProperties implements Serializable {

    private Map<String, String> columnTypeMapping;

}
