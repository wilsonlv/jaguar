package com.jaguar.process.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jaguar.core.base.BaseModel;

/**
 * <p>
 * 按钮实例表
 * </p>
 *
 * @author lvws
 * @since 2019-04-04
 */
@TableName("button_inst")
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
	@TableField("process_definition_id")
	private String processDefinitionId;
    /**
     * 流程任务定义ID
     */
	@TableField("task_def_id")
	private String taskDefId;
    /**
     * 排序号
     */
	@TableField("sort_no")
	private Integer sortNo;

	@TableField(exist = false)
	private ButtonDef buttonDef;


	public Long getButtonDefId() {
		return buttonDefId;
	}

	public void setButtonDefId(Long buttonDefId) {
		this.buttonDefId = buttonDefId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getTaskDefId() {
		return taskDefId;
	}

	public void setTaskDefId(String taskDefId) {
		this.taskDefId = taskDefId;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public ButtonDef getButtonDef() {
		return buttonDef;
	}

	public void setButtonDef(ButtonDef buttonDef) {
		this.buttonDef = buttonDef;
	}
}