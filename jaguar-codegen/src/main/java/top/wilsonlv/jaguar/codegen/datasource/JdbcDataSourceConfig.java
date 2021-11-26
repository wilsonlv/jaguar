package top.wilsonlv.jaguar.codegen.datasource;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author lvws
 * @since 2021/6/17
 */
@Configuration
public class JdbcDataSourceConfig {

    @Resource
    private DynamicDataSourceProperties dataSourceProperties;

    @Bean
    public JdbcDataSourceProvider dataSourceProvider() {
        DataSourceProperty dataSourceProperty = dataSourceProperties.getDatasource().get(dataSourceProperties.getPrimary());
        return new JdbcDataSourceProvider(dataSourceProperty.getDriverClassName(),
                dataSourceProperty.getUrl(), dataSourceProperty.getUsername(), dataSourceProperty.getPassword());
    }

}
