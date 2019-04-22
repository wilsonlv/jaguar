package com.itqingning.jaguar.mybatisplus;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by lvws on 2019/4/17.
 */
@Configuration
@MapperScan("com.**.mapper*")
@PropertySource("classpath:mybatis-plus.properties")
@EnableTransactionManagement(proxyTargetClass = true)
@EnableConfigurationProperties(MybatisPlusProperties.class)
public class MybatisPlusConfig {

    @Autowired
    private MybatisPlusProperties mybatisPlusProperties;

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
        return new MybatisMapperRefresh(mybatisPlusProperties.getMapperLocations(), sqlSessionFactory,
                10, 5, mybatisPlusProperties.getRefreshMapper());
    }

}
