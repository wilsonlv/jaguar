package org.jaguar.modules.codegen.config;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.Data;
import org.jaguar.commons.basecrud.BaseController;
import org.jaguar.commons.basecrud.BaseMapper;
import org.jaguar.commons.basecrud.BaseModel;
import org.jaguar.commons.basecrud.BaseService;
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
//        config.setEntity("org/jaguar/modules/codegen/template/entity.java.vm");
//        config.setMapper("org/jaguar/modules/codegen/template/mapper.java.vm");
//        config.setXml("org/jaguar/modules/codegen/template/mapper.xml.vm");
//        config.setService(null);
//        config.setServiceImpl("org/jaguar/modules/codegen/template/service.java.vm");
//        config.setController("org/jaguar/modules/codegen/template/controller.java.vm");
        return config;
    }

}
