package com.jaguar.process.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jaguar.core.base.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 表单模版表
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
@TableName("form_template")
public class FormTemplate extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * xml元素ID
     */
    @TableField("element_id")
    private String elementId;
    /**
     * 表单名称
     */
    @TableField("name_")
    private String name;
    /**
     * 版本
     */
    @TableField("version_")
    private Integer version;

    @TableField(exist = false)
    private List<FormTemplateSheet> formTemplateSheets = new ArrayList<>();

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<FormTemplateSheet> getFormTemplateSheets() {
        return formTemplateSheets;
    }

    public void setFormTemplateSheets(List<FormTemplateSheet> formTemplateSheets) {
        this.formTemplateSheets = formTemplateSheets;
    }
}