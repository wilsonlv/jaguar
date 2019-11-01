package org.jaguar.modules.workflow.service;


import org.apache.commons.lang3.StringUtils;
import org.jaguar.commons.aviator.ExpressionUtil;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.commons.utils.DateUtil;
import org.jaguar.core.base.BaseService;
import org.jaguar.modules.workflow.mapper.FormTemplateFieldMapper;
import org.jaguar.modules.workflow.model.po.FormTemplateField;
import org.jaguar.modules.workflow.model.po.FormTemplateRow;
import org.jaguar.modules.workflow.model.po.FormTemplateSheet;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 表单字段表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
@Service
public class FormTemplateFieldService extends BaseService<FormTemplateField, FormTemplateFieldMapper> {

    /**
     * 根据表单ID和字段key查询表单字段
     */
    public FormTemplateField getByFormIdAndFieldKey(Long formTemplateId, String fieldKey) {
        JaguarLambdaQueryWrapper<FormTemplateField> wrapper = JaguarLambdaQueryWrapper.newInstance();
        wrapper.eq(FormTemplateField::getFormTemplateId, formTemplateId)
                .eq(FormTemplateField::getKey, fieldKey);
        return this.unique(wrapper);
    }

    /**
     * 在目标表单块中根据表单字段key查询表单字段
     */
    public FormTemplateField findByKey(FormTemplateSheet sheet, String key) {
        for (FormTemplateRow row : sheet.getFormTemplateRows()) {
            for (FormTemplateField field : row.getFormTemplateFields()) {
                if (field.getKey().equals(key)) {
                    return field;
                }
            }
        }
        return null;
    }

    /**
     * 解析表单字段的表达式值
     */
    public void resolveExpressionValue(FormTemplateSheet sheet, Map<String, Object> evn) {
        for (FormTemplateRow row : sheet.getFormTemplateRows()) {
            for (FormTemplateField field : row.getFormTemplateFields()) {
                if (StringUtils.isNotBlank(field.getDefaultValue()) && ExpressionUtil.isExpression(field.getDefaultValue())) {
                    Object result = ExpressionUtil.execute(field.getDefaultValue(), evn);
                    if (result instanceof Date) {
                        field.setDefaultValue(DateUtil.format((Date) result, DateUtil.DateTimePattern.valueOf(field.getTimePattern())));
                    } else {
                        field.setDefaultValue((String) result);
                    }
                }
            }
        }
    }
}
