package com.jaguar.flowable.model;

import com.jaguar.flowable.enums.TaskFieldPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lvws on 2019/3/1.
 */
public class UserTask extends Node {

    private String assignee;
    private String candidateGroup;
    private List<String> formKeys;
    private Map<String, TaskFieldPermission> taskFormPermissionMap = new HashMap<>();
    private List<FlowableEvent> flowableEvents = new ArrayList<>();

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
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
