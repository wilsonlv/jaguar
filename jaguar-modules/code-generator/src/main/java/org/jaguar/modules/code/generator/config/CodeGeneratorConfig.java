package org.jaguar.modules.code.generator.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.Data;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.base.BaseMapper;
import org.jaguar.core.base.BaseModel;
import org.jaguar.core.base.BaseService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lvws on 2019/4/30.
 */
@Data
@Configuration
@ConfigurationProperties("spring.datasource")
public class CodeGeneratorConfig {

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
        strategy.setSuperControllerClass(AbstractController.class.getName());
        // controller mapping 驼峰转连字符
        strategy.setControllerMappingHyphenStyle(true);
        return strategy;
    }

    @Bean
    public PackageConfig packageConfig() {
        PackageConfig config = new PackageConfig();
        config.setEntity("model");
        config.setMapper("mapper");
        config.setXml("mapper.xml");
        config.setService(null);
        config.setServiceImpl("service");
        config.setController("controller");
        return config;
    }

    @Bean
    public TemplateConfig templateConfig() {
        TemplateConfig config = new TemplateConfig();
        config.setEntity("org/jaguar/modules/code/generator/template/entity.java.vm");
        config.setMapper("org/jaguar/modules/code/generator/template/mapper.java.vm");
        config.setXml("org/jaguar/modules/code/generator/template/mapper.xml.vm");
        config.setService(null);
        config.setServiceImpl("org/jaguar/modules/code/generator/template/service.java.vm");
        config.setController("org/jaguar/modules/code/generator/template/controller.java.vm");
        return config;
    }

    /*@Bean
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
    }*/


}
