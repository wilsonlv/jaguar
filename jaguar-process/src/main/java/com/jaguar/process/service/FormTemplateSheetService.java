package com.jaguar.process.service;

import com.jaguar.core.base.BaseService;
import com.jaguar.core.util.Assert;
import com.jaguar.core.util.InstanceUtil;
import com.jaguar.process.mapper.FormTemplateSheetMapper;
import com.jaguar.process.model.po.FormTemplateRow;
import com.jaguar.process.model.po.FormTemplateSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 表单块表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
@Service
@CacheConfig(cacheNames = "FormTemplateSheet")
public class FormTemplateSheetService extends BaseService<FormTemplateSheet, FormTemplateSheetMapper> {

    @Autowired
    private FormTemplateRowService formTemplateRowService;
    @Autowired
    private FormTemplateFieldService formTemplateFieldService;

    /**
     * 根据表单ID查询表单块列表
     */
    public List<FormTemplateSheet> queryListByFormTemplateId(Long formTemplateId) {
        Map<String, Object> param = InstanceUtil.newHashMap();
        param.put("formTemplateId", formTemplateId);
        return this.queryList(param);
    }

    /**
     * 根据表单ID和表单块元素ID查询表单块
     */
    public FormTemplateSheet getByFormIdAndElementId(Long formTemplateId, String elementId) {
        Map<String, Object> param = InstanceUtil.newHashMap();
        param.put("formTemplateId", formTemplateId);
        param.put("elementId", elementId);
        return this.unique(param);
    }

    /**
     * 根据ID查询表单块及其全部组件
     */
    public FormTemplateSheet getSheetComponentById(Long id) {
        FormTemplateSheet sheet = this.getById(id);
        Assert.validateId(sheet, "表单块", id);

        fillSheetComponent(sheet);
        return sheet;
    }

    /**
     * 填充表单块的全部组件
     */
    public void fillSheetComponent(FormTemplateSheet sheet) {
        Map<String, Object> param = InstanceUtil.newHashMap();
        param.put("formTemplateSheetId", sheet.getId());
        sheet.setFormTemplateRows(formTemplateRowService.queryList(param));

        for (FormTemplateRow row : sheet.getFormTemplateRows()) {
            param.put("formTemplateRowId", row.getId());
            row.setFormTemplateFields(formTemplateFieldService.queryList(param));
        }
    }


}
