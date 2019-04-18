package com.itqingning.jaguar.mybatisplus;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisMapperRefresh;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by lvws on 2019/4/17.
 */
@Configuration
@MapperScan("com.**.mapper*")
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource("classpath:mybatis-plus.properties")
public class MybatisPlusConfig {

    @Value("${mybatis-plus.mapper-locations}")
    private Resource[] mapperLocations;
    @Value("${mybatis-plus.refresh}")
    private Boolean refresh;

    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public MybatisMapperRefresh mybatisMapperRefresh(SqlSessionFactory sqlSessionFactory) {
        return new MybatisMapperRefresh(mapperLocations, sqlSessionFactory, refresh);
    }

}
