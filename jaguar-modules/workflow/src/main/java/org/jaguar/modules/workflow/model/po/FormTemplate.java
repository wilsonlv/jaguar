package org.jaguar.modules.workflow.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jaguar.core.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
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
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_process_form_template")
public class FormTemplate extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * xml元素ID
     */
    @NotBlank(message = "表单模版元素ID")
    @TableField("element_id")
    private String elementId;
    /**
     * 表单名称
     */
    @NotBlank(message = "表单名称")
    @TableField("name_")
    private String name;
    /**
     * 版本
     */
    @TableField("version_")
    private Integer version;

    @TableField(exist = false)
    private List<FormTemplateSheet> formTemplateSheets = new ArrayList<>();

}