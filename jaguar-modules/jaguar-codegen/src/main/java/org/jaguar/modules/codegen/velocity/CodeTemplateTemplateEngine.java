package org.jaguar.modules.codegen.velocity;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.jaguar.modules.codegen.service.CodeGeneratorService;
import org.jaguar.modules.codegen.service.CodeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Properties;

/**
 * @author lvws
 * @since 2021/6/17
 */
@Component
public class CodeTemplateTemplateEngine extends AbstractTemplateEngine {

    @Autowired
    private CodeTemplateService codeTemplateService;

    private VelocityEngine velocityEngine;

    @Override
    public CodeTemplateTemplateEngine init(ConfigBuilder configBuilder) {
        super.init(configBuilder);
        if (null == velocityEngine) {
            Properties p = new Properties();
            p.setProperty(ConstVal.VM_LOAD_PATH_KEY, CodeTemplateResourceLoader.class.getName());
            p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, StringPool.EMPTY);
            p.setProperty(Velocity.ENCODING_DEFAULT, ConstVal.UTF8);
            p.setProperty(Velocity.INPUT_ENCODING, ConstVal.UTF8);
            p.setProperty("file.resource.loader.unicode", StringPool.TRUE);
            velocityEngine = new VelocityEngine(p);
        }
        return this;
    }

    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        Template template = velocityEngine.getTemplate(templatePath, ConstVal.UTF8);

        ByteArrayOutputStream outputStream = CodeGeneratorService.TEMPLATE_PREVIEW_OUTPUT_STREAM.get();
        if (outputStream != null) {
            try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream)) {
                template.merge(new VelocityContext(objectMap), outputStreamWriter);
            }
        } else {
            try (FileOutputStream fos = new FileOutputStream(outputFile);
                 OutputStreamWriter ow = new OutputStreamWriter(fos, ConstVal.UTF8);
                 BufferedWriter writer = new BufferedWriter(ow)) {
                template.merge(new VelocityContext(objectMap), writer);
            }
            logger.debug("模板:" + templatePath + ";  文件:" + outputFile);
        }
    }

    @Override
    public String templateFilePath(String filePath) {
        return filePath;
    }
}
