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
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jaguar.commons.basecrud.BaseController;
import org.jaguar.commons.basecrud.BaseMapper;
import org.jaguar.commons.basecrud.BaseModel;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.modules.codegen.component.VelocityTemplateEngine;
import org.jaguar.modules.codegen.controller.dto.Codegen;
import org.jaguar.modules.codegen.controller.vo.TableVO;
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
@RequiredArgsConstructor
public class CodeGeneratorService {

    private final GlobalConfig globalConfig;

    private final PackageConfig packageConfig;

    private final TemplateConfig templateConfig;

    private final DataSourceService dataSourceService;

    private final VelocityTemplateEngine templateEngine;

    private final DynamicRoutingDataSource dynamicRoutingDataSource;

    @DS("#dataSourceName")
    public Page<TableVO> showTables(Page<TableVO> page, String dataSourceName, String fuzzyTableName) {
        ItemDataSource dataSource = (ItemDataSource) dynamicRoutingDataSource.getDataSource(dataSourceName);
        DruidDataSource realDataSource = (DruidDataSource) dataSource.getRealDataSource();
        String url = realDataSource.getUrl().split("\\?")[0];
        String schema = url.substring(url.lastIndexOf('/') + 1);
        return dataSourceService.showTables(page, schema, fuzzyTableName);
    }


    public StrategyConfig createStrategyConfig(Codegen codegen) {
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

    @DS("#codegen.dataSourceName")
    public void generate(Codegen codegen, HttpServletResponse response) throws IOException {
        String tempDir = "temp" + File.separator + System.currentTimeMillis() + File.separator + codegen.getTableName();

        globalConfig.setAuthor(codegen.getAuthor());
        globalConfig.setOutputDir(tempDir);

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
        generator.setStrategy(createStrategyConfig(codegen));
        // 包配置
        generator.setPackageInfo(packageConfig);
        // 模版配置
        generator.setTemplate(templateConfig);
        // 模板引擎
        generator.setTemplateEngine(templateEngine);
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
