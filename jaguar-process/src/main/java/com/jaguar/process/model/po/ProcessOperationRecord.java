package com.jaguar.process.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jaguar.core.base.BaseModel;
import com.jaguar.process.enums.ProcessOperationType;

import java.util.Date;

/**
 * <p>
 * 流程操作记录表
 * </p>
 *
 * @author lvws
 * @since 2019-04-10
 */
@TableName("process_operation_record")
public class ProcessOperationRecord extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 工单信息ID
     */
    @TableField("process_info_id")
    private Long processInfoId;
    /**
     * 操作人账号
     */
    @TableField("operator_")
    private String operator;
    /**
     * 操作类型
     */
    @TableField("process_operation_type")
    private ProcessOperationType processOperationType;
    /**
     * 操作时间
     */
    @TableField("operate_time")
    private Date operateTime;
    /**
     * 任务名称
     */
    @TableField("task_name")
    private String taskName;
    /**
     * 任务实例ID
     */
    @TableField("task_inst_id")
    private String taskInstId;
    /**
     * 任务定义ID
     */
    @TableField("task_def_id")
    private String taskDefId;
    /**
     * 受派人账号
     */
    @TableField("assignee_")
    private String assignee;


    public Long getProcessInfoId() {
        return processInfoId;
    }

    public void setProcessInfoId(Long processInfoId) {
        this.processInfoId = processInfoId;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public ProcessOperationType getProcessOperationType() {
        return processOperationType;
    }

    public void setProcessOperationType(ProcessOperationType processOperationType) {
        this.processOperationType = processOperationType;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskInstId() {
        return taskInstId;
    }

    public void setTaskInstId(String taskInstId) {
        this.taskInstId = taskInstId;
    }

    public String getTaskDefId() {
        return taskDefId;
    }

    public void setTaskDefId(String taskDefId) {
        this.taskDefId = taskDefId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}