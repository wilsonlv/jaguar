package com.jaguar.mybatisplus.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lvws on 2019/4/30.
 */
@Configuration
@ConfigurationProperties("spring.datasource")
public class GeneratorConfig {

    private String driverClassName;
    private String username;
    private String password;
    private String url;

    @Bean("generatorGlobalConfig")
    public GlobalConfig globalConfig() {
        GlobalConfig config = new GlobalConfig();
        config.setFileOverride(true);
        config.setBaseResultMap(true);
        config.setOpen(false);
        config.setServiceImplName("%sService");
        return config;
    }

    @Bean
    public DataSourceConfig dataSourceConfig() {
        DataSourceConfig config = new DataSourceConfig();
        config.setDbType(DbType.MYSQL);
        config.setDriverName(driverClassName);
        config.setUsername(username);
        config.setPassword(password);
        config.setUrl(url);
        return config;
    }

    @Bean
    public StrategyConfig strategyConfig() {
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 字段名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 自定义实体父类
        strategy.setSuperEntityClass("com.jaguar.core.base.BaseModel");
        // 自定义实体，公共字段
        strategy.setSuperEntityColumns("id_", "deleted_", "remark_", "create_by", "create_time", "update_by", "update_time");
        // 自定义 mapper 父类
        strategy.setSuperMapperClass("com.jaguar.core.base.BaseMapper");
        // 自定义 service 父类
        strategy.setSuperServiceClass("com.jaguar.core.base.BaseService");
        // 自定义 service 实现类父类
        strategy.setSuperServiceImplClass("com.jaguar.core.base.BaseService");
        // 自定义 controller 父类
        strategy.setSuperControllerClass("com.jaguar.core.base.AbstractController");
        return strategy;
    }

    @Bean
    public PackageConfig packageConfig() {
        PackageConfig config = new PackageConfig();
        config.setEntity("model");
        config.setMapper("mapper");
        config.setXml("mapper.xml");
        config.setServiceImpl("service");
        config.setController("controller");
        return config;
    }

    @Bean
    public TemplateConfig templateConfig() {
        TemplateConfig config = new TemplateConfig();
        config.setEntity("com/jaguar/mybatisplus/generator/template/entity.java.vm");
        config.setMapper("com/jaguar/mybatisplus/generator/template/mapper.java.vm");
        config.setXml("com/jaguar/mybatisplus/generator/template/mapper.xml.vm");
        config.setServiceImpl("com/jaguar/mybatisplus/generator/template/service.java.vm");
        config.setController("com/jaguar/mybatisplus/generator/template/controller.java.vm");
        return config;
    }

    @Bean
    public AutoGenerator autoGenerator(GlobalConfig globalConfig, DataSourceConfig dataSourceConfig,
                                       StrategyConfig strategyConfig, PackageConfig packageConfig,
                                       TemplateConfig templateConfig) {

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
        return generator;
    }


}
