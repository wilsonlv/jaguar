package org.jaguar.modules.workflow.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jaguar.core.base.BaseModel;
import org.jaguar.modules.workflow.enums.ProcessOperationType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 工单操作记录表
 * </p>
 *
 * @author lvws
 * @since 2019-04-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_process_operation_record")
public class OperationRecord extends BaseModel {

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

}