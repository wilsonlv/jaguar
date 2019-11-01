package org.jaguar.modules.workflow.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.core.base.BaseModel;
import org.jaguar.modules.workflow.enums.DefinitionType;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author lvws
 * @since 2019-03-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_workflow_draft_definition")
public class DraftDefinition extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 表单模版ID
     */
    @TableField("element_id")
    private String elementId;
    /**
     * 名称
     */
    @TableField("name_")
    private String name;
    /**
     * 类型（FORM：表单，FLOW：流程）
     */
    @TableField("definition_type")
    private DefinitionType definitionType;
    /**
     * 内容
     */
    @TableField("context_")
    private String context;
    /**
     * 版本
     */
    @TableField("version_")
    private Integer version;

}