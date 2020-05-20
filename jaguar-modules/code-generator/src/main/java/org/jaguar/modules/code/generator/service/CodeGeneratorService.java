package org.jaguar.modules.code.generator.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import org.apache.commons.lang3.StringUtils;
import org.jaguar.core.base.BaseService;
import org.jaguar.modules.code.generator.config.CodeGeneratorConfig;
import org.jaguar.modules.code.generator.mapper.CodeGeneratorMapper;
import org.jaguar.modules.code.generator.model.CodeGenerator;
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

    private String schema;

    @Autowired
    public void setSchema(CodeGeneratorConfig codeGeneratorConfig) {
        String url = codeGeneratorConfig.getUrl().split("\\?")[0];
        this.schema = url.substring(url.lastIndexOf('/') + 1);
    }

    public Page<CodeGenerator> showTables(Page<CodeGenerator> page, String fuzzyTableName) {
        return mapper.showTables(page, schema, fuzzyTableName);
    }

    public void generate(CodeGenerator codeGenerator) {
        globalConfig.setAuthor(codeGenerator.getAuthor());
        strategyConfig.setInclude(codeGenerator.getTableName());
        packageConfig.setParent(codeGenerator.getParentPackage());
        globalConfig.setOutputDir(codeGenerator.getOutputDir());

        if (StringUtils.isNotBlank(codeGenerator.getModuleName())) {
            packageConfig.setModuleName(codeGenerator.getModuleName());
            strategyConfig.setTablePrefix("t_" + codeGenerator.getModuleName());
        } else {
            strategyConfig.setTablePrefix("t_");
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
