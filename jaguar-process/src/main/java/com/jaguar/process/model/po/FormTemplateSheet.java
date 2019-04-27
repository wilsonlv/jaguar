package com.jaguar.process.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jaguar.core.base.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 表单块表
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
@TableName("form_template_sheet")
public class FormTemplateSheet extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * xml元素ID
     */
    @TableField("element_id")
    private String elementId;
    /**
     * 表单模版ID
     */
    @TableField("form_template_id")
    private Long formTemplateId;
    /**
     * 表单块名称
     */
    @TableField("name_")
    private String name;
    /**
     * 排序号
     */
    @TableField("sort_no")
    private Integer sortNo;

    @TableField(exist = false)
    private List<FormTemplateRow> formTemplateRows = new ArrayList<>();

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public Long getFormTemplateId() {
        return formTemplateId;
    }

    public void setFormTemplateId(Long formTemplateId) {
        this.formTemplateId = formTemplateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public List<FormTemplateRow> getFormTemplateRows() {
        return formTemplateRows;
    }

    public void setFormTemplateRows(List<FormTemplateRow> formTemplateRows) {
        this.formTemplateRows = formTemplateRows;
    }
}