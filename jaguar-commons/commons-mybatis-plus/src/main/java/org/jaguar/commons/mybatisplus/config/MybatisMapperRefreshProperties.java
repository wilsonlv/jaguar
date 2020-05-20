package org.jaguar.commons.mybatisplus.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lvws
 * @since 2019/5/23.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "mybatis-plus")
public class MybatisMapperRefreshProperties {

    private boolean mapperRefresh;

    private String mapperLocations;

}
