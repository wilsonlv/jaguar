package org.jaguar.modules.code.generator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lvws
 * @since 2019/11/7
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jaguar.code-generator")
public class CodeGeneratorProperties {

    private String defaultOutputDir;
    private String defaultParentPackage;

}
