package top.wilsonlv.jaguar.codegen.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import top.wilsonlv.jaguar.codegen.mapper.DataSourceMapper;
import top.wilsonlv.jaguar.codegen.entity.DataSource;
import top.wilsonlv.jaguar.commons.basecrud.Assert;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;
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

    public DruidDataSource getPrimary() {
        return this.getDataSource(dataSourceProperties.getPrimary());
    }

    public DruidDataSource getDataSource(String dataSourceName) {
        ItemDataSource dataSource = (ItemDataSource) dynamicRoutingDataSource.getDataSource(dataSourceName);
        return (DruidDataSource) dataSource.getRealDataSource();
    }

    public String getSchema(String dataSourceName) {
        DruidDataSource druidDataSource = this.getDataSource(dataSourceName);
        String url = druidDataSource.getUrl().split("\\?")[0];
        return url.substring(url.lastIndexOf('/') + 1);
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

}
