package com.jaguar.process.model.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lvws on 2019/3/27.
 */
public class TaskDTO implements Serializable {

    private String taskDefId;
    private String taskId;
    private String taskName;
    private String assignee;
    private Date startTime;
    private Date endTime;

    public TaskDTO() {
    }

    public String getTaskDefId() {
        return taskDefId;
    }

    public void setTaskDefId(String taskDefId) {
        this.taskDefId = taskDefId;
    }

    public TaskDTO(String taskName) {
        this.taskName = taskName;
    }

    public TaskDTO(String taskId, String taskName) {
        this.taskId = taskId;
        this.taskName = taskName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
