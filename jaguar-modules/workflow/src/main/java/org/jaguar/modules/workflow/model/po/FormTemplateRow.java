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
@TableName("t_process_form_template_row")
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

}