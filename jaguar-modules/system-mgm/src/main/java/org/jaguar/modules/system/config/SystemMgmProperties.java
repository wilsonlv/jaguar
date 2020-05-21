package org.jaguar.modules.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lvws
 * @since 2020/5/21
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jaguar.modules.system.mgm")
public class SystemMgmProperties {

    private Boolean verfiyCodeEnable;

}
