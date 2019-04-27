package com.jaguar.process.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jaguar.core.base.BaseModel;
import com.jaguar.process.enums.FormTemplateFieldType;
import com.jaguar.process.model.vo.component.ComponentConfig;

/**
 * <p>
 * 表单字段表
 * </p>
 *
 * @author lvws
 * @since 2019-03-01
 */
@TableName("form_template_field")
public class FormTemplateField extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 表单模版ID
     */
    @TableField("form_template_id")
    private Long formTemplateId;
    /**
     * 表单块ID
     */
    @TableField("form_template_sheet_id")
    private Long formTemplateSheetId;
    /**
     * 表单行ID
     */
    @TableField("form_template_row_id")
    private Long formTemplateRowId;
    /**
     * 字段标签
     */
    @TableField("label_")
    private String label;
    /**
     * 字段key
     */
    @TableField("key_")
    private String key;
    /**
     * 字段类型
     */
    @TableField("form_template_field_type")
    private FormTemplateFieldType formTemplateFieldType;
    /**
     * 提示信息
     */
    @TableField("placeholder_")
    private String placeholder;
    /**
     * 结果或结果表达式
     */
    @TableField("result_value")
    private String resultValue;
    /**
     * 默认值或默认值表达式
     */
    @TableField("default_value")
    private String defaultValue;
    /**
     * 焦点离开事件
     */
    @TableField("foucs_out_event")
    private String foucsOutEvent;
    /**
     * 排序号
     */
    @TableField("sort_no")
    private Integer sortNo;
    /**
     * 数据字典类型
     */
    @TableField("dic_type")
    private String dicType;
    /**
     * 时间格式
     */
    @TableField("time_pattern")
    private String timePattern;
    /**
     * 组件配置
     */
    @TableField("component_config")
    private String componentConfig;

    @TableField(exist = false)
    private ComponentConfig config;

    @TableField(exist = false)
    private String value;

    public Long getFormTemplateId() {
        return formTemplateId;
    }

    public void setFormTemplateId(Long formTemplateId) {
        this.formTemplateId = formTemplateId;
    }

    public Long getFormTemplateSheetId() {
        return formTemplateSheetId;
    }

    public void setFormTemplateSheetId(Long formTemplateSheetId) {
        this.formTemplateSheetId = formTemplateSheetId;
    }

    public Long getFormTemplateRowId() {
        return formTemplateRowId;
    }

    public void setFormTemplateRowId(Long formTemplateRowId) {
        this.formTemplateRowId = formTemplateRowId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public FormTemplateFieldType getFormTemplateFieldType() {
        return formTemplateFieldType;
    }

    public void setFormTemplateFieldType(FormTemplateFieldType formTemplateFieldType) {
        this.formTemplateFieldType = formTemplateFieldType;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getFoucsOutEvent() {
        return foucsOutEvent;
    }

    public void setFoucsOutEvent(String foucsOutEvent) {
        this.foucsOutEvent = foucsOutEvent;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getDicType() {
        return dicType;
    }

    public void setDicType(String dicType) {
        this.dicType = dicType;
    }

    public String getTimePattern() {
        return timePattern;
    }

    public void setTimePattern(String timePattern) {
        this.timePattern = timePattern;
    }

    public String getComponentConfig() {
        return componentConfig;
    }

    public void setComponentConfig(String componentConfig) {
        this.componentConfig = componentConfig;
    }

    public ComponentConfig getConfig() {
        return config;
    }

    public void setConfig(ComponentConfig config) {
        this.config = config;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}