package org.jaguar.modules.workflow.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jaguar.core.base.BaseService;
import org.jaguar.core.exception.Assert;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.modules.workflow.Constant;
import org.jaguar.modules.workflow.mapper.FormTemplateMapper;
import org.jaguar.modules.workflow.model.po.FormTemplate;
import org.jaguar.modules.workflow.model.po.FormTemplateField;
import org.jaguar.modules.workflow.model.po.FormTemplateRow;
import org.jaguar.modules.workflow.model.po.FormTemplateSheet;
import org.jaguar.modules.workflow.util.Bpmn20Util;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 表单模版表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
@Service
public class FormTemplateService extends BaseService<FormTemplate, FormTemplateMapper> {

    @Autowired
    private FormTemplateSheetService formTemplateSheetService;
    @Autowired
    private FormTemplateRowService formTemplateRowService;
    @Autowired
    private FormTemplateFieldService formTemplateFieldService;

    /**
     * 根据表单查询最新的表单基本信息
     */
    private FormTemplate getLatest(String name, String elementId) {

        JaguarLambdaQueryWrapper<FormTemplate> wrapper = new JaguarLambdaQueryWrapper<>();
        wrapper.eq(FormTemplate::getName, name);
        wrapper.eq(FormTemplate::getElementId, elementId);
        wrapper.orderByDesc(FormTemplate::getVersion);

        return this.unique(wrapper, false);
    }

    /**
     * 发布表单
     */
    @Transactional
    public FormTemplate deployForm(FormTemplate formTemplate) {
        formTemplate.setId(null);
        formTemplate.setVersion(null);

        FormTemplate latestByName = this.getLatest(formTemplate.getName(), null);
        formTemplate.setVersion(latestByName == null ? 1 : latestByName.getVersion() + 1);
        FormTemplate persistentForm = this.insert(formTemplate);

        for (int i = 0; i < formTemplate.getFormTemplateSheets().size(); i++) {
            FormTemplateSheet sheet = formTemplate.getFormTemplateSheets().get(i);

            Assert.notNull(sheet.getElementId(), "表单块元素ID");
            Assert.notNull(sheet.getName(), "表单块名称");

            sheet.setId(null);
            sheet.setFormTemplateId(persistentForm.getId());
            sheet.setSortNo(i);
            FormTemplateSheet persistentSheet = formTemplateSheetService.insert(sheet);
            persistentForm.getFormTemplateSheets().add(persistentSheet);

            for (int j = 0; j < sheet.getFormTemplateRows().size(); j++) {
                FormTemplateRow row = sheet.getFormTemplateRows().get(j);

                row.setId(null);
                row.setFormTemplateId(persistentForm.getId());
                row.setFormTemplateSheetId(persistentSheet.getId());
                row.setSortNo(j);
                FormTemplateRow persistentRow = formTemplateRowService.insert(row);
                persistentSheet.getFormTemplateRows().add(persistentRow);

                for (int k = 0; k < row.getFormTemplateFields().size(); k++) {
                    FormTemplateField field = row.getFormTemplateFields().get(k);

                    Assert.notNull(field.getKey(), "表单字段key");
                    Assert.notNull(field.getLabel(), "表单字段标签");
                    Assert.notNull(field.getFormTemplateFieldType(), "表单字段类型");

                    field.setId(null);
                    field.setFormTemplateId(persistentForm.getId());
                    field.setFormTemplateSheetId(persistentSheet.getId());
                    field.setFormTemplateRowId(persistentRow.getId());
                    field.setSortNo(k);

                    if (StringUtils.isNotBlank(field.getComponentConfig())) {
                        JSONObject jsonObject = JSONObject.parseObject(field.getComponentConfig());
                        field.setComponentConfig(jsonObject.toJSONString());
                    }

                    FormTemplateField persistentField = formTemplateFieldService.insert(field);
                    persistentRow.getFormTemplateFields().add(persistentField);
                }
            }
        }

        return persistentForm;
    }

    /**
     * 查询最新版的表单基本信息
     */
    public IPage<FormTemplate> queryLatest(IPage<FormTemplate> page, String name, String fuzzyName, String elementId) {
        page.setRecords(mapper.queryLatest(page, name, fuzzyName, elementId));
        return page;
    }

    /**
     * 获取完整的表单组件
     */
    @Transactional
    public FormTemplate getFormComponentById(Long id) {
        FormTemplate formTemplate = this.getById(id);
        Assert.validateId(formTemplate, "表单模版", id);

        this.fillFormComponent(formTemplate);
        return formTemplate;
    }

    /**
     * 获取完整的表单组件
     */
    @Transactional
    public void fillFormComponent(FormTemplate formTemplate) {

        JaguarLambdaQueryWrapper<FormTemplateSheet> sheetWrapper = new JaguarLambdaQueryWrapper<>();

        sheetWrapper.eq(FormTemplateSheet::getFormTemplateId, formTemplate.getId());
        formTemplate.setFormTemplateSheets(formTemplateSheetService.list(sheetWrapper));

        for (FormTemplateSheet sheet : formTemplate.getFormTemplateSheets()) {

            sheet.setFormTemplateRows(formTemplateRowService.list(JaguarLambdaQueryWrapper.<FormTemplateRow>newInstance()
                    .eq(FormTemplateRow::getFormTemplateSheetId, sheet.getId())));

            for (FormTemplateRow row : sheet.getFormTemplateRows()) {
                row.setFormTemplateFields(formTemplateFieldService.list(JaguarLambdaQueryWrapper.<FormTemplateField>newInstance()
                        .eq(FormTemplateField::getFormTemplateRowId, row.getId())));
            }
        }
    }

    /**
     * 根据表单元素ID获取最新版的完整表单组件
     */
    @Transactional
    public FormTemplate getFormComponentByElementId(String elementId) {
        FormTemplate formTemplate = this.getLatest(null, elementId);
        this.fillFormComponent(formTemplate);
        return formTemplate;
    }

    /**
     * 根据流程bpmn规范查询所绑定的表单基本信息
     */
    public FormTemplate findByBpmn(BpmnModel bpmnModel) {
        String fromTemplateElementId = Bpmn20Util.findProcessProperties(bpmnModel)
                .getString(Constant.PROCESS_PROPERTIES_FORM_ELEMENT_ID);
        return this.getLatest(null, fromTemplateElementId);
    }

}
