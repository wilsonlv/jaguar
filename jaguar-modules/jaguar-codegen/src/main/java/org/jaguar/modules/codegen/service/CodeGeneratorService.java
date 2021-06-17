package org.jaguar.modules.codegen.service;


import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import org.apache.commons.lang3.StringUtils;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.modules.codegen.controller.dto.CodeGenerator;
import org.jaguar.modules.codegen.mapper.CodeGeneratorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvws
 * @since 2019/4/30.
 */
@Service
public class CodeGeneratorService extends BaseService<CodeGenerator, CodeGeneratorMapper> {

    @Autowired
    private GlobalConfig globalConfig;
    @Autowired
    private DataSourceConfig dataSourceConfig;
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
    public Page<CodeGenerator> showTables(Page<CodeGenerator> page, String dataSourceName, String fuzzyTableName) {
        ItemDataSource dataSource = (ItemDataSource) dynamicRoutingDataSource.getDataSource(dataSourceName);
        DruidDataSource realDataSource = (DruidDataSource) dataSource.getRealDataSource();
        String rawJdbcUrl = realDataSource.getRawJdbcUrl();
        String url = rawJdbcUrl.split("\\?")[0];
        String schema = url.substring(url.lastIndexOf('/') + 1);
        return mapper.showTables(page, schema, fuzzyTableName);
    }

    public void generate(CodeGenerator codeGenerator) {
        globalConfig.setAuthor(codeGenerator.getAuthor());
        strategyConfig.setInclude(codeGenerator.getTableName());
        packageConfig.setParent(codeGenerator.getParentPackage());
        globalConfig.setOutputDir(codeGenerator.getOutputDir());

        if (StringUtils.isNotBlank(codeGenerator.getModuleName())) {
            packageConfig.setModuleName(codeGenerator.getModuleName());
            strategyConfig.setTablePrefix(codeGenerator.getTablePrefix() + codeGenerator.getModuleName());
        } else {
            strategyConfig.setTablePrefix(codeGenerator.getTablePrefix());
        }

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

        generator.execute();
    }
}
