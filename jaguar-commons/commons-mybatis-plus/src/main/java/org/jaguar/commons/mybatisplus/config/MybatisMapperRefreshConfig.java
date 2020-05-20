package org.jaguar.commons.mybatisplus.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.jaguar.commons.mybatisplus.extension.MybatisMapperRefresh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

/**
 * @author lvws
 * @since 2019/5/23.
 */
@Configuration
public class MybatisMapperRefreshConfig {

    @Autowired
    private MybatisMapperRefreshProperties mybatisMapperRefreshProperties;

    @Bean
    public MybatisMapperRefresh mybatisMapperRefresh(SqlSessionFactory sqlSessionFactory) throws IOException {
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources(mybatisMapperRefreshProperties.getMapperLocations());
        return new MybatisMapperRefresh(resources, sqlSessionFactory, mybatisMapperRefreshProperties.isMapperRefresh());
    }

}
