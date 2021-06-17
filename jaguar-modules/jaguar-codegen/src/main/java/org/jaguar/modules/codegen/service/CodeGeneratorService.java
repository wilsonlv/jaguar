package org.jaguar.modules.codegen.service;


import cn.hutool.core.util.ZipUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import org.apache.commons.io.IOUtils;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.modules.codegen.controller.dto.Codegen;
import org.jaguar.modules.codegen.controller.vo.TableVO;
import org.jaguar.modules.codegen.mapper.DataSourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author lvws
 * @since 2019/4/30.
 */
@Service
public class CodeGeneratorService  {

    @Autowired
    private GlobalConfig globalConfig;
    @Autowired
    private StrategyConfig strategyConfig;
    @Autowired
    private PackageConfig packageConfig;
    @Autowired
    private TemplateConfig templateConfig;
    @Autowired
    private DataSourceService dataSourceService;
    @Autowired
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    @DS("#dataSourceName")
    public Page<TableVO> showTables(Page<TableVO> page, String dataSourceName, String fuzzyTableName) {
        ItemDataSource dataSource = (ItemDataSource) dynamicRoutingDataSource.getDataSource(dataSourceName);
        DruidDataSource realDataSource = (DruidDataSource) dataSource.getRealDataSource();
        String url = realDataSource.getUrl().split("\\?")[0];
        String schema = url.substring(url.lastIndexOf('/') + 1);
        return dataSourceService.showTables(page, schema, fuzzyTableName);
    }

    @DS("#codeGenerator.dataSourceName")
    public void generate(Codegen codegen, HttpServletResponse response) throws IOException {
        String tempDir = "temp" + File.separator + System.currentTimeMillis() + File.separator + codegen.getTableName();

        globalConfig.setAuthor(codegen.getAuthor());
        globalConfig.setOutputDir(tempDir);

        strategyConfig.setInclude(codegen.getTableName());
        strategyConfig.setTablePrefix(codegen.getTablePrefix());

        packageConfig.setParent(codegen.getParentPackage());
        packageConfig.setModuleName(codegen.getModuleName());

        ItemDataSource dataSource = (ItemDataSource) dynamicRoutingDataSource.getDataSource(codegen.getDataSourceName());
        DruidDataSource realDataSource = (DruidDataSource) dataSource.getRealDataSource();

        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setDriverName(realDataSource.getDriverClassName());
        dataSourceConfig.setUsername(realDataSource.getUsername());
        dataSourceConfig.setPassword(realDataSource.getPassword());
        dataSourceConfig.setUrl(realDataSource.getUrl());

        AutoGenerator generator = new AutoGenerator();
        // 全局配置
        generator.setGlobalConfig(globalConfig);
        // 数据源配置
        generator.setDataSource(dataSourceConfig);
        // 策略配置
        generator.setStrategy(strategyConfig);
        // 包配置
        generator.setPackageInfo(packageConfig);
        // 模版配置
        generator.setTemplate(templateConfig);
        //生成代码
        generator.execute();

        File zip = ZipUtil.zip(new File(tempDir));

        response.setHeader("Content-Disposition", "attachment;filename=" + zip.getName());

        try (FileInputStream inputStream = new FileInputStream(zip);
             ServletOutputStream outputStream = response.getOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
        }
    }
}
