package org.jaguar.modules.codegen.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.jaguar.commons.basecrud.Assert;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.commons.web.exception.impl.CheckedException;
import org.jaguar.modules.codegen.mapper.DataSourceMapper;
import org.jaguar.modules.codegen.model.DataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lvws
 * @since 2021-06-16
 */
@Service
@RequiredArgsConstructor
public class DataSourceService extends BaseService<DataSource, DataSourceMapper> {

    private static final String MASTER = "master";

    private static final String JDBC_URL = "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&serverTimezone=PRC&useSSL=false";

    public DataSource getByName(String name) {
        return this.unique(Wrappers.<DataSource>lambdaQuery()
                .eq(DataSource::getName, name));
    }

    public static String getJdbcUrl(String host, String port, String schema){
        return String.format(JDBC_URL, host, port, schema);
    }

    @Transactional
    public void save(DataSource dataSource) {
        if (MASTER.equals(dataSource.getName())) {
            throw new CheckedException("数据源名称不能为" + MASTER);
        }

        DataSource byName = this.getByName(dataSource.getName());
        Assert.duplicate(byName, dataSource, "数据源名称");
        this.saveOrUpdate(dataSource);
    }
}
