package com.jaguar.process.model.vo;

import com.jaguar.process.Constant;
import com.jaguar.process.enums.TaskFieldPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lvws on 2019/3/1.
 */
public class UserTask extends Node {

    /**
     * 单一执行人
     */
    private String assignee;
    /**
     * 候选组
     */
    private String candidateGroup;

    /**
     * 多执行人
     */
    private String multiInstanceAssignee;
    private String completionCondition = Constant.DEFAULT_COMPLETION_CONDITION;
    private Boolean sequential = false;

    private List<String> formKeys;
    private Map<String, TaskFieldPermission> taskFormPermissionMap = new HashMap<>();
    private List<FlowableEvent> flowableEvents = new ArrayList<>();

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getMultiInstanceAssignee() {
        return multiInstanceAssignee;
    }

    public void setMultiInstanceAssignee(String multiInstanceAssignee) {
        this.multiInstanceAssignee = multiInstanceAssignee;
    }

    public String getCompletionCondition() {
        return completionCondition;
    }

    public void setCompletionCondition(String completionCondition) {
        this.completionCondition = completionCondition;
    }

    public Boolean getSequential() {
        return sequential;
    }

    public void setSequential(Boolean sequential) {
        this.sequential = sequential;
    }

    public String getCandidateGroup() {
        return candidateGroup;
    }

    public void setCandidateGroup(String candidateGroup) {
        this.candidateGroup = candidateGroup;
    }

    public List<String> getFormKeys() {
        return formKeys;
    }

    public void setFormKeys(List<String> formKeys) {
        this.formKeys = formKeys;
    }

    public List<FlowableEvent> getFlowableEvents() {
        return flowableEvents;
    }

    public void setFlowableEvents(List<FlowableEvent> flowableEvents) {
        this.flowableEvents = flowableEvents;
    }

    public Map<String, TaskFieldPermission> getTaskFormPermissionMap() {
        return taskFormPermissionMap;
    }

    public void setTaskFormPermissionMap(Map<String, TaskFieldPermission> taskFormPermissionMap) {
        this.taskFormPermissionMap = taskFormPermissionMap;
    }
}
