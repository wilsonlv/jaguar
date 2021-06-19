package org.jaguar.modules.codegen.velocity;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.jaguar.commons.web.exception.impl.CheckedException;
import org.jaguar.modules.codegen.enums.CodeTemplateType;
import org.jaguar.modules.codegen.model.CodeTemplate;
import org.jaguar.modules.codegen.service.CodeGeneratorService;
import org.jaguar.modules.codegen.service.CodeTemplateService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author lvws
 * @since 2021/6/17
 */
public class CodeTemplateResourceLoader extends ResourceLoader {

    @Override
    public void init(ExtendedProperties configuration) {
    }

    @Override
    public InputStream getResourceStream(String source) throws ResourceNotFoundException {
        String codeTemplateFile = CodeGeneratorService.TEMPLATE_PREVIEW_FILE.get();
        if (codeTemplateFile == null) {
            for (CodeTemplateType value : CodeTemplateType.values()) {
                if (value.name().equals(source)) {
                    CodeTemplate codeTemplate = CodeTemplateService.CODE_TEMPLATE_DATA_BASE.get(value);
                    if (codeTemplate == null) {
                        throw new CheckedException("没有找到" + source + "模板");
                    }
                    codeTemplateFile = codeTemplate.getCodeTemplateFile();
                    break;
                }
            }

            if (codeTemplateFile == null) {
                return null;
            }
        }

        return new ByteArrayInputStream(codeTemplateFile.getBytes(StandardCharsets.UTF_8));
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
