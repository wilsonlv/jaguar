package com.itqingning.jaguar.mybatisplus;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * Created by lvws on 2019/4/22.
 */
@Configuration
@ConfigurationProperties(prefix = "mybatis-plus")
public class MybatisPlusProperties {

    private Resource[] mapperLocations;
    private Boolean refreshMapper;

    public Resource[] getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(Resource[] mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public Boolean getRefreshMapper() {
        return refreshMapper;
    }

    public void setRefreshMapper(Boolean refreshMapper) {
        this.refreshMapper = refreshMapper;
    }
}
