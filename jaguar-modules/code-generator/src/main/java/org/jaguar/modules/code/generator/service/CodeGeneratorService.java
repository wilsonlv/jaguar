package org.jaguar.modules.code.generator.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import org.jaguar.core.base.BaseService;
import org.jaguar.modules.code.generator.mapper.CodeGeneratorMapper;
import org.jaguar.modules.code.generator.model.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by lvws on 2019/4/30.
 */
@Service
public class CodeGeneratorService extends BaseService<CodeGenerator, CodeGeneratorMapper> {

    @Autowired
    private AutoGenerator generator;
    @Autowired
    private GlobalConfig globalConfig;
    @Autowired
    private StrategyConfig strategyConfig;
    @Autowired
    private PackageConfig packageConfig;

    private String schema;

    @Autowired
    public void setSchema(@Value("${spring.datasource.url}") String url) {
        url = url.split("\\?")[0];
        this.schema = url.substring(url.lastIndexOf('/') + 1);
    }

    public Page<CodeGenerator> showTables(Page<CodeGenerator> page, String fuzzyTableName) {
        page.setRecords(mapper.showTables(page, schema, fuzzyTableName));
        return page;
    }

    public void generate(CodeGenerator codeGenerator) {
        globalConfig.setAuthor(codeGenerator.getAuthor());
        globalConfig.setOutputDir(codeGenerator.getOutputDir());
        strategyConfig.setInclude(codeGenerator.getTableName());

        packageConfig.setParent(codeGenerator.getParentPackage());
        packageConfig.setModuleName(codeGenerator.getModuleName());

        generator.execute();
    }
}
