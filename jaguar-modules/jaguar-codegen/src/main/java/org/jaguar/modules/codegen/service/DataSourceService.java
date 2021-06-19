package org.jaguar.modules.codegen.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.jaguar.commons.basecrud.Assert;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.commons.web.exception.impl.CheckedException;
import org.jaguar.modules.codegen.controller.vo.TableVO;
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

    private static final String JDBC_URL = "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&serverTimezone=PRC&useSSL=false";

    public static String getJdbcUrl(String host, String port, String schema) {
        return String.format(JDBC_URL, host, port, schema);
    }

    private final DynamicRoutingDataSource dynamicRoutingDataSource;

    private final DynamicDataSourceProperties dataSourceProperties;

    private final DefaultDataSourceCreator dataSourceCreator;

    public String getSchema(String dataSourceName){
        DruidDataSource druidDataSource = this.getDataSource(dataSourceName);
        String url = druidDataSource.getUrl().split("\\?")[0];
        return url.substring(url.lastIndexOf('/') + 1);
    }

    public DruidDataSource getPrimary() {
        return this.getDataSource(dataSourceProperties.getPrimary());
    }

    public DruidDataSource getDataSource(String dataSourceName) {
        ItemDataSource dataSource = (ItemDataSource) dynamicRoutingDataSource.getDataSource(dataSourceName);
        return (DruidDataSource) dataSource.getRealDataSource();
    }

    public DataSource getByName(String name) {
        return this.unique(Wrappers.<DataSource>lambdaQuery()
                .eq(DataSource::getName, name));
    }


    @Transactional
    public void save(DataSource dataSource) {
        if (dataSourceProperties.getPrimary().equals(dataSource.getName())) {
            throw new CheckedException("数据源名称不能为" + dataSourceProperties.getPrimary());
        }

        DataSource byName = this.getByName(dataSource.getName());
        Assert.duplicate(byName, dataSource, "数据源名称");
        this.saveOrUpdate(dataSource);

        DataSourceProperty mainDataSourceProperty = dataSourceProperties.getDatasource().get(dataSourceProperties.getPrimary());

        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setUrl(getJdbcUrl(dataSource.getHost(), dataSource.getPort(), dataSource.getSchema()));
        dataSourceProperty.setUsername(dataSource.getUsername());
        dataSourceProperty.setPassword(dataSource.getPassword());
        dataSourceProperty.setDriverClassName(mainDataSourceProperty.getDriverClassName());
        javax.sql.DataSource newDataSource = dataSourceCreator.createDataSource(dataSourceProperty);

        dynamicRoutingDataSource.addDataSource(dataSource.getName(), newDataSource);
    }

    @Transactional
    public void del(Long id) {
        DataSource dataSource = this.getById(id);
        dynamicRoutingDataSource.removeDataSource(dataSource.getName());
        this.delete(id);
    }

    public Page<TableVO> showTables(Page<TableVO> page, String dataSourceName, String fuzzyTableName) {
        String schema = this.getSchema(dataSourceName);
        return this.mapper.showTables(page, schema, fuzzyTableName);
    }
}
