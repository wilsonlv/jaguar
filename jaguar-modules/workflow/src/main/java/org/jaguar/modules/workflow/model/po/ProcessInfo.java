package org.jaguar.modules.workflow.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jaguar.core.base.BaseModel;
import org.jaguar.modules.workflow.model.dto.TaskDTO;
import org.jaguar.modules.workflow.model.vo.UserTask;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 工单信息表
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_process_info")
public class ProcessInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 流程定义ID
     */
    @TableField("process_definition_id")
    private String processDefinitionId;
    /**
     * 流程实例ID
     */
    @TableField("process_instance_id")
    private String processInstanceId;
    /**
     * 表单模版ID
     */
    @TableField("form_template_id")
    private Long formTemplateId;
    /**
     * 工单编号
     */
    @TableField("process_num")
    private String processNum;
    /**
     * 发起人（账号）
     */
    @TableField("initiator_")
    private String initiator;
    /**
     * 工单标题
     */
    @TableField("title_")
    private String title;
    /**
     * 优先级（1：非常紧急，2：紧急，3-50：普通）
     */
    @TableField("priority_")
    private Integer priority;
    /**
     * 下单时间
     */
    @TableField("order_time")
    private Date orderTime;


    /**
     * 表单模版
     */
    @TableField(exist = false)
    private FormTemplate formTemplate;
    /**
     * 流程定义名称
     */
    @TableField(exist = false)
    private String processDefinitionName;
    /**
     * 工单结束时间
     */
    @TableField(exist = false)
    private Date endTime;
    /**
     * 当前用户任务
     */
    @TableField(exist = false)
    private UserTask userTask;
    /**
     * 工单的待办任务
     */
    @TableField(exist = false)
    private TaskDTO taskDTO;
    /**
     * 工单的已办任务
     */
    @TableField(exist = false)
    private Map<String, String> taskDTOS = new LinkedHashMap<>();

}