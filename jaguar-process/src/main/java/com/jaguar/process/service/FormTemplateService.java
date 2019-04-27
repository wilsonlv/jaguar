package com.jaguar.process.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jaguar.core.base.BaseService;
import com.jaguar.core.enums.OrderType;
import com.jaguar.core.util.Assert;
import com.jaguar.core.util.InstanceUtil;
import com.jaguar.process.Constant;
import com.jaguar.process.util.Bpmn20Util;
import com.jaguar.process.mapper.FormTemplateMapper;
import com.jaguar.process.model.po.FormTemplate;
import com.jaguar.process.model.po.FormTemplateField;
import com.jaguar.process.model.po.FormTemplateRow;
import com.jaguar.process.model.po.FormTemplateSheet;
import org.flowable.bpmn.model.BpmnModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.jaguar.core.constant.Constant.*;

/**
 * <p>
 * 表单模版表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
@Service
@CacheConfig(cacheNames = "FormTemplate")
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
        Map<String, Object> param = InstanceUtil.newHashMap();
        param.put(PAGE, 1);
        param.put(ROWS, 1);
        param.put(SORT, "version_");
        param.put(ORDER, OrderType.DESC);
        param.put("name", name);
        param.put("elementId", elementId);
        List<FormTemplate> records = this.query(param).getRecords();
        if (records.size() > 0) {
            return records.get(0);
        }
        return null;
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
        FormTemplate persistentForm = this.update(formTemplate);

        for (int i = 0; i < formTemplate.getFormTemplateSheets().size(); i++) {
            FormTemplateSheet sheet = formTemplate.getFormTemplateSheets().get(i);

            Assert.notNull(sheet.getElementId(), "表单块元素ID");
            Assert.notNull(sheet.getName(), "表单块名称");

            sheet.setId(null);
            sheet.setFormTemplateId(persistentForm.getId());
            sheet.setSortNo(i);
            FormTemplateSheet persistentSheet = formTemplateSheetService.update(sheet);
            persistentForm.getFormTemplateSheets().add(persistentSheet);

            for (int j = 0; j < sheet.getFormTemplateRows().size(); j++) {
                FormTemplateRow row = sheet.getFormTemplateRows().get(j);

                row.setId(null);
                row.setFormTemplateId(persistentForm.getId());
                row.setFormTemplateSheetId(persistentSheet.getId());
                row.setSortNo(j);
                FormTemplateRow persistentRow = formTemplateRowService.update(row);
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
                    FormTemplateField persistentField = formTemplateFieldService.update(field);
                    persistentRow.getFormTemplateFields().add(persistentField);
                }
            }
        }

        return persistentForm;
    }

    /**
     * 查询最新版的表单基本信息
     */
    public Page<FormTemplate> queryLatest(Map<String, Object> param) {
        Page<Long> page = getPage(param);
        page.setRecords(mapper.queryLatest(page, param));
        return getPage(page);
    }

    /**
     * 获取完整的表单组件
     */
    public FormTemplate getFormComponentById(Long id) {
        FormTemplate formTemplate = this.getById(id);
        Assert.validateId(formTemplate, "表单模版", id);

        this.fillFormComponent(formTemplate);
        return formTemplate;
    }

    /**
     * 获取完整的表单组件
     */
    public void fillFormComponent(FormTemplate formTemplate) {
        Map<String, Object> param = InstanceUtil.newHashMap();
        param.put("formTemplateId", formTemplate.getId());
        formTemplate.setFormTemplateSheets(formTemplateSheetService.queryList(param));

        for (FormTemplateSheet sheet : formTemplate.getFormTemplateSheets()) {
            param.put("formTemplateSheetId", sheet.getId());
            sheet.setFormTemplateRows(formTemplateRowService.queryList(param));

            for (FormTemplateRow row : sheet.getFormTemplateRows()) {
                param.put("formTemplateRowId", row.getId());
                row.setFormTemplateFields(formTemplateFieldService.queryList(param));
            }
        }
    }

    /**
     * 根据表单元素ID获取最新版的完整表单组件
     */
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
