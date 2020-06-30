package org.jaguar.modules.workflow.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jaguar.core.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("jaguar_modules_workflow_form_template_row")
public class FormTemplateRow extends BaseModel implements Cloneable {

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

    @Override
    public FormTemplateRow clone() throws CloneNotSupportedException {
        FormTemplateRow clone = (FormTemplateRow) super.clone();
        clone.setFormTemplateFields(new ArrayList<>());
        for (FormTemplateField field : this.formTemplateFields) {
            clone.getFormTemplateFields().add(field.clone());
        }
        return clone;
    }
}