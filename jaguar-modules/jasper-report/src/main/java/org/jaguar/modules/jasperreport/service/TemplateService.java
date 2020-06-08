package org.jaguar.modules.jasperreport.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.BaseService;
import org.jaguar.core.exception.Assert;
import org.jaguar.modules.document.model.Document;
import org.jaguar.modules.document.service.DocumentService;
import org.jaguar.modules.jasperreport.mapper.TemplateMapper;
import org.jaguar.modules.jasperreport.model.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * jasperReport模板表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2020-06-03
 */
@Service
public class TemplateService extends BaseService<Template, TemplateMapper> {

    @Autowired
    private DocumentService documentService;

    public Template getByName(String name) {
        return this.unique(JaguarLambdaQueryWrapper.<Template>newInstance()
                .eq(Template::getTemplateName, name));
    }

    public Page<Template> queryWithDocument(Page<Template> page, LambdaQueryWrapper<Template> wrapper) {
        page = this.query(page, wrapper);
        for (Template template : page.getRecords()) {
            if (template.getDocumentId() != null) {
                Document document = documentService.getById(template.getDocumentId());
                template.setDocument(document);
            }
        }
        return page;
    }

    @Transactional
    public Template createOrUpdate(Template template) {
        Template byName = this.getByName(template.getTemplateName());
        Assert.duplicate(byName, template, "模板名称");
        return this.saveOrUpdate(template);
    }

    @Transactional
    public Template upload(Long id, MultipartFile file) {
        Template template = this.getById(id);
        Assert.validateId(template, "模板", id);

        Document document = documentService.upload(file);
        template.setDocumentId(document.getId());
        return this.updateById(template);
    }
}
