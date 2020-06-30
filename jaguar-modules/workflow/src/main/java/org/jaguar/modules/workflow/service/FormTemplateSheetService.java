package org.jaguar.modules.workflow.service;

import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.BaseService;
import org.jaguar.core.exception.Assert;
import org.jaguar.modules.workflow.mapper.FormTemplateSheetMapper;
import org.jaguar.modules.workflow.model.po.FormTemplateField;
import org.jaguar.modules.workflow.model.po.FormTemplateRow;
import org.jaguar.modules.workflow.model.po.FormTemplateSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 表单块表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
@Service
public class FormTemplateSheetService extends BaseService<FormTemplateSheet, FormTemplateSheetMapper> {

    @Autowired
    private FormTemplateRowService formTemplateRowService;
    @Autowired
    private FormTemplateFieldService formTemplateFieldService;

    /**
     * 根据表单ID查询表单块列表
     */
    public List<FormTemplateSheet> queryListByFormTemplateId(Long formTemplateId) {
        return this.list(JaguarLambdaQueryWrapper.<FormTemplateSheet>newInstance()
                .eq(FormTemplateSheet::getFormTemplateId, formTemplateId));
    }

    /**
     * 根据表单ID和表单块元素ID查询表单块
     */
    public FormTemplateSheet getByFormIdAndElementId(Long formTemplateId, String elementId) {
        JaguarLambdaQueryWrapper<FormTemplateSheet> wrapper = JaguarLambdaQueryWrapper.newInstance();
        wrapper.eq(FormTemplateSheet::getFormTemplateId, formTemplateId);
        wrapper.eq(FormTemplateSheet::getElementId, elementId);
        return this.unique(wrapper);
    }

    /**
     * 根据ID查询表单块及其全部组件
     */
    @Transactional
    public FormTemplateSheet getSheetComponentById(Long id) {
        FormTemplateSheet sheet = this.getById(id);
        Assert.validateId(sheet, "表单块", id);

        fillSheetComponent(sheet);
        return sheet;
    }

    /**
     * 填充表单块的全部组件
     */
    @Transactional
    public void fillSheetComponent(FormTemplateSheet sheet) {

        List<FormTemplateRow> rows = formTemplateRowService.list(JaguarLambdaQueryWrapper.<FormTemplateRow>newInstance()
                .eq(FormTemplateRow::getFormTemplateSheetId, sheet.getId()));
        sheet.setFormTemplateRows(rows);

        for (FormTemplateRow row : sheet.getFormTemplateRows()) {
            row.setFormTemplateFields(formTemplateFieldService.list(JaguarLambdaQueryWrapper.<FormTemplateField>newInstance()
                    .eq(FormTemplateField::getFormTemplateRowId, row.getId())));
        }
    }


}
