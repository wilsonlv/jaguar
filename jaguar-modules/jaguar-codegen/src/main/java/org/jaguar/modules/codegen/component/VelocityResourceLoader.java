package org.jaguar.modules.codegen.component;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.jaguar.commons.web.exception.impl.CheckedException;
import org.jaguar.modules.codegen.enums.CodeTemplateType;
import org.jaguar.modules.codegen.model.CodeTemplate;
import org.jaguar.modules.codegen.service.CodeTemplateService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author lvws
 * @since 2021/6/17
 */
public class VelocityResourceLoader extends ResourceLoader {

    @Override
    public void init(ExtendedProperties configuration) {

    }

    @Override
    public InputStream getResourceStream(String source) throws ResourceNotFoundException {
        CodeTemplate codeTemplate = CodeTemplateService.CODE_TEMPLATE_DATA_BASE.get(CodeTemplateType.valueOf(source));
        if (codeTemplate == null) {
            throw new CheckedException("没有找到" + source + "模板");
        }

        return new ByteArrayInputStream(codeTemplate.getCodeTemplateFile().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean isSourceModified(Resource resource) {
        return false;
    }

    @Override
    public long getLastModified(Resource resource) {
        return 0;
    }
}
