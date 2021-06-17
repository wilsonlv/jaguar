package org.jaguar.modules.codegen.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.modules.codegen.component.VelocityTemplateEngine;
import org.jaguar.modules.codegen.enums.CodeTemplateType;
import org.jaguar.modules.codegen.mapper.CodeTemplateMapper;
import org.jaguar.modules.codegen.model.CodeTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lvws
 * @since 2021-06-17
 */
@Service
@RequiredArgsConstructor
public class CodeTemplateService extends BaseService<CodeTemplate, CodeTemplateMapper> {

    public List<CodeTemplate> findLatest() {
        return this.mapper.findLatest();
    }

    @Transactional
    public void modify(CodeTemplateType codeTemplateType, String codeTemplateFile) {
        CodeTemplate latest = this.unique(Wrappers.<CodeTemplate>lambdaQuery()
                .eq(CodeTemplate::getCodeTemplateType, codeTemplateFile)
                .orderByDesc(CodeTemplate::getCodeTemplateVersion)
                .last(LIMIT_1));
        int version = latest != null ? latest.getCodeTemplateVersion() + 1 : 1;

        CodeTemplate codeTemplate = new CodeTemplate();
        codeTemplate.setCodeTemplateType(codeTemplateType);
        codeTemplate.setCodeTemplateFile(codeTemplateFile);
        codeTemplate.setCodeTemplateVersion(version);
        this.insert(codeTemplate);

        VelocityTemplateEngine.CODE_TEMPLATE_DATA_BASE.put(codeTemplateType, codeTemplate);
    }
}
