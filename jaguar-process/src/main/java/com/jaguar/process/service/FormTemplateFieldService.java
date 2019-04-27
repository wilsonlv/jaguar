package com.jaguar.process.service;

import com.jaguar.process.mapper.FormTemplateFieldMapper;
import com.jaguar.process.model.po.FormTemplateField;
import com.jaguar.process.model.po.FormTemplateRow;
import com.jaguar.process.model.po.FormTemplateSheet;
import com.jaguar.aviator.ExpressionUtil;
import com.jaguar.core.base.BaseService;
import com.jaguar.core.util.DateUtil;
import com.jaguar.core.util.InstanceUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
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
@CacheConfig(cacheNames = "FormTemplateField")
public class FormTemplateFieldService extends BaseService<FormTemplateField, FormTemplateFieldMapper> {

    /**
     * 根据表单ID和字段key查询表单字段
     */
    public FormTemplateField getByFormIdAndFieldKey(Long formTemplateId, String fieldKey) {
        Map<String, Object> param = InstanceUtil.newHashMap();
        param.put("formTemplateId", formTemplateId);
        param.put("key", fieldKey);
        return this.unique(param);
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
                if (StringUtils.isNotBlank(field.getDefaultValue())) {
                    if (ExpressionUtil.isExpression(field.getDefaultValue())) {
                        Object result = ExpressionUtil.execute(field.getDefaultValue(), evn);
                        if (result instanceof Date) {
                            field.setDefaultValue(DateUtil.format(result, field.getTimePattern()));
                        } else {
                            field.setDefaultValue((String) result);
                        }
                    }
                }

                /*if (field.getFormTemplateFieldType().equals(FormTemplateFieldType.datetime)) {
                    if (StringUtils.isNotBlank(field.getMaxDate()) && ExpressionUtil.isExpression(field.getMaxDate())) {
                        Object result = ExpressionUtil.execute(field.getMaxDate(), evn);
                        if (result != null) {
                            field.setMaxDate((String) result);
                        }
                    }
                    if (StringUtils.isNotBlank(field.getMinDate()) && ExpressionUtil.isExpression(field.getMinDate())) {
                        Object result = ExpressionUtil.execute(field.getMinDate(), evn);
                        field.setMinDate((String) result);
                    }
                }*/
            }
        }
    }
}
