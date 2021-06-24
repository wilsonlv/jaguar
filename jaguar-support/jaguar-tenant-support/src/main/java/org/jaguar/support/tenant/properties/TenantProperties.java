package org.jaguar.support.tenant.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.List;

/**
 * @author lvws
 * @since 2021/6/24
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jaguar.tenant")
public class TenantProperties implements Serializable {

    private String tenantIdColumn = "tenantId";

    private List<String> ignoreTables;

}
