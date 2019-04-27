package com.jaguar.process.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jaguar.core.base.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 表单行表
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
@TableName("form_template_row")
public class FormTemplateRow extends BaseModel {

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
     * 排序号
     */
    @TableField("sort_no")
    private Integer sortNo;

    @TableField(exist = false)
    private List<FormTemplateField> formTemplateFields = new ArrayList<>();


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

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public List<FormTemplateField> getFormTemplateFields() {
        return formTemplateFields;
    }

    public void setFormTemplateFields(List<FormTemplateField> formTemplateFields) {
        this.formTemplateFields = formTemplateFields;
    }
}