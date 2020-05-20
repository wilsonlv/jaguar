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
 * 表单块表
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("jaguar_modules_workflow_form_template_sheet")
public class FormTemplateSheet extends BaseModel implements Cloneable {

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
     * 数据重写
     */
    @TableField("override_")
    private Boolean override;
    /**
     * 排序号
     */
    @TableField("sort_no")
    private Integer sortNo;

    @TableField(exist = false)
    private List<FormTemplateRow> formTemplateRows = new ArrayList<>();

    @Override
    public FormTemplateSheet clone() throws CloneNotSupportedException {
        FormTemplateSheet clone = (FormTemplateSheet) super.clone();
        clone.setFormTemplateRows(new ArrayList<>());
        for (FormTemplateRow row : this.getFormTemplateRows()) {
            clone.getFormTemplateRows().add(row.clone());
        }
        return clone;
    }
}