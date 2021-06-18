package org.jaguar.modules.codegen.velocity;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import lombok.Data;
import org.jaguar.modules.codegen.enums.CodeTemplateType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lvws
 * @since 2019/4/30.
 */
@Data
@Configuration
@ConfigurationProperties("spring.datasource")
public class CodegenConfig {

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
        config.setEntity(CodeTemplateType.ENTITY.name());
        config.setMapper(CodeTemplateType.MAPPER.name());
        config.setXml(CodeTemplateType.MAPPER_XML.name());
        config.setService(null);
        config.setServiceImpl(CodeTemplateType.SERVICE.name());
        config.setController(CodeTemplateType.CONTROLLER.name());
        return config;
    }

}
