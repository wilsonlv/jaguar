package org.jaguar.support.tenant.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lvws
 * @since 2021/6/5
 */
@Configuration
public class TenantConfig {

    @Bean
    public TenantLineHandler tenantLineHandler() {
        return new JaguarTenantLineHandler();
    }

}
