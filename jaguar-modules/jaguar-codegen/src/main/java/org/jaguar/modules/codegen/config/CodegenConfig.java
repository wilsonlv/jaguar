package org.jaguar.modules.codegen.config;

import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import org.jaguar.modules.codegen.enums.CodeTemplateType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lvws
 * @since 2019/4/30.
 */
@Configuration
public class CodegenConfig {

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
