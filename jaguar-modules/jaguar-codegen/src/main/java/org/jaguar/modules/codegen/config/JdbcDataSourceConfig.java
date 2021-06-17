package org.jaguar.modules.codegen.config;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.jaguar.modules.codegen.component.JdbcDataSourceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lvws
 * @since 2021/6/17
 */
@Configuration
public class JdbcDataSourceConfig {

    @Autowired
    private DynamicDataSourceProperties dataSourceProperties;

    @Bean
    public JdbcDataSourceProvider dataSourceProvider() {
        DataSourceProperty dataSourceProperty = dataSourceProperties.getDatasource().get(dataSourceProperties.getPrimary());
        return new JdbcDataSourceProvider(dataSourceProperty.getUrl(), dataSourceProperty.getUsername(),
                dataSourceProperty.getPassword(), dataSourceProperty.getDriverClassName());
    }

}
