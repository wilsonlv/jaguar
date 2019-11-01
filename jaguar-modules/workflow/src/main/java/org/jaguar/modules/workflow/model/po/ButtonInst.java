package org.jaguar.modules.workflow.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jaguar.core.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 按钮实例表
 * </p>
 *
 * @author lvws
 * @since 2019-04-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_workflow_button_inst")
public class ButtonInst extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 按钮定义ID
     */
    @TableField("button_def_id")
    private Long buttonDefId;
    /**
     * 流程定义ID
     */
    @TableField("process_definition_key")
    private String processDefinitionkey;
    /**
     * 流程任务定义ID
     */
    @TableField("task_def_name")
    private String taskDefName;
    /**
     * 排序号
     */
    @TableField("sort_no")
    private Integer sortNo;
    /**
     * 参数
     */
    @TableField("params_")
    private String params;

    @TableField(exist = false)
    private ButtonDef buttonDef;

}