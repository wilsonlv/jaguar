package com.jaguar.process.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jaguar.core.base.BaseModel;
import com.jaguar.process.model.dto.TaskDTO;
import com.jaguar.process.model.vo.UserTask;

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
@TableName("process_info")
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
     * 流程编号
     */
    @TableField("process_num")
    private String processNum;
    /**
     * 发起人（账号）
     */
    @TableField("initiator_")
    private String initiator;
    /**
     * 流程名称
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

    @TableField(exist = false)
    private FormTemplate formTemplate;
    @TableField(exist = false)
    private String processDefinitionName;
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


    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Long getFormTemplateId() {
        return formTemplateId;
    }

    public void setFormTemplateId(Long formTemplateId) {
        this.formTemplateId = formTemplateId;
    }

    public String getProcessNum() {
        return processNum;
    }

    public void setProcessNum(String processNum) {
        this.processNum = processNum;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public UserTask getUserTask() {
        return userTask;
    }

    public void setUserTask(UserTask userTask) {
        this.userTask = userTask;
    }

    public FormTemplate getFormTemplate() {
        return formTemplate;
    }

    public void setFormTemplate(FormTemplate formTemplate) {
        this.formTemplate = formTemplate;
    }

    public TaskDTO getTaskDTO() {
        return taskDTO;
    }

    public void setTaskDTO(TaskDTO taskDTO) {
        this.taskDTO = taskDTO;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Map<String, String> getTaskDTOS() {
        return taskDTOS;
    }

    public void setTaskDTOS(Map<String, String> taskDTOS) {
        this.taskDTOS = taskDTOS;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }
}