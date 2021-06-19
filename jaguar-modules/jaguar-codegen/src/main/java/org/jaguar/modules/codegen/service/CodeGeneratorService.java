package org.jaguar.modules.codegen.service;


import cn.hutool.core.util.ZipUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jaguar.commons.basecrud.BaseController;
import org.jaguar.commons.basecrud.BaseMapper;
import org.jaguar.commons.basecrud.BaseModel;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.modules.codegen.controller.dto.CodegenDTO;
import org.jaguar.modules.codegen.controller.dto.PreviewDTO;
import org.jaguar.modules.codegen.controller.vo.TableVO;
import org.jaguar.modules.codegen.enums.CodeTemplateType;
import org.jaguar.modules.codegen.velocity.CodeTemplateTemplateEngine;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author lvws
 * @since 2019/4/30.
 */
@Service
@RequiredArgsConstructor
public class CodeGeneratorService {

    private final TemplateConfig templateConfig;

    private final DataSourceService dataSourceService;

    private final CodeTemplateTemplateEngine templateEngine;

    private final static String TEMP_DIR = "temp";

    public final static ThreadLocal<String> TEMPLATE_PREVIEW_FILE = new ThreadLocal<>();

    public final static ThreadLocal<ByteArrayOutputStream> TEMPLATE_PREVIEW_OUTPUT_STREAM = new ThreadLocal<>();


    @DS("#dataSourceName")
    public Page<TableVO> showTables(Page<TableVO> page, String dataSourceName, String fuzzyTableName) {
        return dataSourceService.showTables(page, dataSourceName, fuzzyTableName);
    }

    private GlobalConfig createGlobalConfig(String author, String outputDir) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setFileOverride(true);
        globalConfig.setBaseResultMap(true);
        globalConfig.setOpen(false);
        globalConfig.setServiceImplName("%sService");
        globalConfig.setAuthor(author);
        globalConfig.setOutputDir(outputDir);
        return globalConfig;
    }

    private PackageConfig createPackageConfig(CodegenDTO codegen) {
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setEntity("model");
        packageConfig.setMapper("mapper");
        packageConfig.setXml("mapper.xml");
        packageConfig.setService(null);
        packageConfig.setServiceImpl("service");
        packageConfig.setController("controller");
        packageConfig.setParent(codegen.getParentPackage());
        packageConfig.setModuleName(codegen.getModuleName());
        return packageConfig;
    }

    private StrategyConfig createStrategyConfig(CodegenDTO codegen) {
        StrategyConfig strategy = new StrategyConfig();
        // 字段名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 自定义实体，公共字段
        strategy.setSuperEntityColumns("id_", "deleted_", "remark_", "create_by", "create_time", "update_by", "update_time");
        // 自定义实体父类
        strategy.setSuperEntityClass(BaseModel.class.getName());
        // 自定义 mapper 父类
        strategy.setSuperMapperClass(BaseMapper.class.getName());
        // 自定义 service 实现类父类
        strategy.setSuperServiceImplClass(BaseService.class.getName());
        // 自定义 controller 父类
        strategy.setSuperControllerClass(BaseController.class.getName());
        // controller mapping 驼峰转连字符
        strategy.setControllerMappingHyphenStyle(true);
        // 表前缀
        if (StringUtils.isNotBlank(codegen.getTablePrefix())) {
            strategy.setTablePrefix(codegen.getTablePrefix());
        }
        //生成表范围
        strategy.setInclude(codegen.getTableName());
        return strategy;
    }

    private TemplateConfig createTemplate(CodeTemplateType codeTemplateType) {
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setEntity(null);
        templateConfig.setMapper(null);
        templateConfig.setXml(null);
        templateConfig.setService(null);
        templateConfig.setServiceImpl(null);
        templateConfig.setController(null);
        switch (codeTemplateType) {
            case ENTITY: {
                templateConfig.setEntity(CodeTemplateType.ENTITY.name());
                break;
            }
            case MAPPER: {
                templateConfig.setMapper(CodeTemplateType.MAPPER.name());
                break;
            }
            case MAPPER_XML: {
                templateConfig.setXml(CodeTemplateType.MAPPER_XML.name());
                break;
            }
            case SERVICE: {
                templateConfig.setServiceImpl(CodeTemplateType.SERVICE.name());
                break;
            }
            case CONTROLLER: {
                templateConfig.setController(CodeTemplateType.CONTROLLER.name());
                break;
            }
            default:
        }
        return templateConfig;
    }

    private DataSourceConfig createDataSourceConfig(DruidDataSource druidDataSource) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setDriverName(druidDataSource.getDriverClassName());
        dataSourceConfig.setUsername(druidDataSource.getUsername());
        dataSourceConfig.setPassword(druidDataSource.getPassword());
        dataSourceConfig.setUrl(druidDataSource.getUrl());
        return dataSourceConfig;
    }

    @DS("#codegen.dataSourceName")
    public void generate(CodegenDTO codegen, HttpServletResponse response) throws IOException {
        String tempDir = TEMP_DIR + File.separator + System.currentTimeMillis() + File.separator + codegen.getTableName();

        DruidDataSource druidDataSource = dataSourceService.getDataSource(codegen.getDataSourceName());

        AutoGenerator generator = new AutoGenerator();
        // 全局配置
        generator.setGlobalConfig(createGlobalConfig(codegen.getAuthor(), tempDir));
        // 数据源配置
        generator.setDataSource(createDataSourceConfig(druidDataSource));
        // 策略配置
        generator.setStrategy(createStrategyConfig(codegen));
        // 包配置
        generator.setPackageInfo(createPackageConfig(codegen));
        // 模版配置
        generator.setTemplate(templateConfig);
        // 模板引擎
        generator.setTemplateEngine(templateEngine);
        //生成代码
        generator.execute();

        File file = new File(tempDir);
        File zip = ZipUtil.zip(file);

        response.setHeader("Content-Disposition", "attachment;filename=" + zip.getName());

        try (FileInputStream inputStream = new FileInputStream(zip);
             ServletOutputStream outputStream = response.getOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
        }

        file.delete();
        zip.delete();
    }

    public String preview(PreviewDTO preview) {
        DruidDataSource primary = dataSourceService.getPrimary();

        AutoGenerator generator = new AutoGenerator();
        // 全局配置
        generator.setGlobalConfig(createGlobalConfig(preview.getAuthor(), TEMP_DIR));
        // 数据源配置
        generator.setDataSource(createDataSourceConfig(primary));
        // 策略配置
        generator.setStrategy(createStrategyConfig(preview));
        // 包配置
        generator.setPackageInfo(createPackageConfig(preview));
        // 模版配置
        generator.setTemplate(createTemplate(preview.getCodeTemplateType()));
        // 模板引擎
        generator.setTemplateEngine(templateEngine);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TEMPLATE_PREVIEW_FILE.set(preview.getCodeTemplateFile());
        TEMPLATE_PREVIEW_OUTPUT_STREAM.set(outputStream);

        //生成代码
        generator.execute();

        TEMPLATE_PREVIEW_FILE.remove();
        TEMPLATE_PREVIEW_OUTPUT_STREAM.remove();
        return outputStream.toString();
    }


}
