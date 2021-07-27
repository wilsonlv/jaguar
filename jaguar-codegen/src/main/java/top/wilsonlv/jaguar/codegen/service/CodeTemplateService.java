package top.wilsonlv.jaguar.codegen.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import top.wilsonlv.jaguar.codegen.mapper.CodeTemplateMapper;
import top.wilsonlv.jaguar.codegen.model.CodeTemplate;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    public static final Map<String, CodeTemplate> CODE_TEMPLATE_DATA_BASE = new HashMap<>();


    public List<CodeTemplate> findLatest() {
        return this.mapper.findLatest();
    }

    @Transactional
    public void modify(CodeTemplate codeTemplate) {
        CodeTemplate latest = this.unique(Wrappers.<CodeTemplate>lambdaQuery()
                .eq(CodeTemplate::getCodeTemplateName, codeTemplate.getCodeTemplateName())
                .orderByDesc(CodeTemplate::getCodeTemplateVersion)
                .last(LIMIT_1));

        if (latest != null) {
            codeTemplate.setCodeTemplateVersion(latest.getCodeTemplateVersion() + 1);
            codeTemplate.setCreateTime(latest.getCreateTime());
        } else {
            codeTemplate.setCodeTemplateVersion(1);
            codeTemplate.setCreateTime(LocalDateTime.now());
        }
        this.insert(codeTemplate);

        CODE_TEMPLATE_DATA_BASE.put(codeTemplate.getCodeTemplateName(), codeTemplate);
    }

    @Override
    public void afterPropertiesSet() {
        List<CodeTemplate> codeTemplates = this.findLatest();
        for (CodeTemplate codeTemplate : codeTemplates) {
            CODE_TEMPLATE_DATA_BASE.put(codeTemplate.getCodeTemplateName(), codeTemplate);
        }
    }
}
