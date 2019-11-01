package org.jaguar.modules.document.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lvws on 2019/6/25.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jaguar.modules.document")
public class DocumentProperties {

    private String filePath;

    private String tempDir;

}
