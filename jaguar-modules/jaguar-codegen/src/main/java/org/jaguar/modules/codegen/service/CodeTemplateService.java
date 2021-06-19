package org.jaguar.modules.codegen.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.modules.codegen.enums.CodeTemplateType;
import org.jaguar.modules.codegen.mapper.CodeTemplateMapper;
import org.jaguar.modules.codegen.model.CodeTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lvws
 * @since 2021-06-17
 */
@Service
@RequiredArgsConstructor
public class CodeTemplateService extends BaseService<CodeTemplate, CodeTemplateMapper> implements InitializingBean {

    public static final Map<CodeTemplateType, CodeTemplate> CODE_TEMPLATE_DATA_BASE = new HashMap<>();


    public List<CodeTemplate> findLatest() {
        return this.mapper.findLatest();
    }

    @Transactional
    public void modify(CodeTemplateType codeTemplateType, String codeTemplateFile) {
        CodeTemplate latest = this.unique(Wrappers.<CodeTemplate>lambdaQuery()
                .eq(CodeTemplate::getCodeTemplateType, codeTemplateType)
                .orderByDesc(CodeTemplate::getCodeTemplateVersion)
                .last(LIMIT_1));
        int version = latest != null ? latest.getCodeTemplateVersion() + 1 : 1;

        CodeTemplate codeTemplate = new CodeTemplate();
        codeTemplate.setCodeTemplateType(codeTemplateType);
        codeTemplate.setCodeTemplateFile(codeTemplateFile);
        codeTemplate.setCodeTemplateVersion(version);
        this.insert(codeTemplate);

        CODE_TEMPLATE_DATA_BASE.put(codeTemplateType, codeTemplate);
    }

    @Override
    public void afterPropertiesSet() {
        List<CodeTemplate> codeTemplates = this.findLatest();
        for (CodeTemplate codeTemplate : codeTemplates) {
            CODE_TEMPLATE_DATA_BASE.put(codeTemplate.getCodeTemplateType(), codeTemplate);
        }
    }
}
