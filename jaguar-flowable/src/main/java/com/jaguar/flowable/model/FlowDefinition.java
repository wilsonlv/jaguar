package com.jaguar.flowable.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lvws on 2019/3/1.
 */
public class FlowDefinition implements Serializable {

    private String id;
    private String name;
    private String initiateTask;

    private String description;
    private Integer version;

    private StartState startState;
    private EndState endState;

    private List<UserTask> userTaskList = new ArrayList<>();
    private List<ServiceTask> serviceTaskList = new ArrayList<>();
    private List<ExclusiveGateway> exclusiveGatewayList = new ArrayList<>();
    private List<ParallelGateway> parallelGatewayList = new ArrayList<>();

    private Map<String, NodeBox> nodeBoxList = new HashMap<>();
    private List<Line> lineList = new ArrayList<>();

    private String formElementId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInitiateTask() {
        return initiateTask;
    }

    public void setInitiateTask(String initiateTask) {
        this.initiateTask = initiateTask;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public StartState getStartState() {
        return startState;
    }

    public void setStartState(StartState startState) {
        this.startState = startState;
    }

    public EndState getEndState() {
        return endState;
    }

    public void setEndState(EndState endState) {
        this.endState = endState;
    }

    public List<UserTask> getUserTaskList() {
        return userTaskList;
    }

    public void setUserTaskList(List<UserTask> userTaskList) {
        this.userTaskList = userTaskList;
    }

    public List<ServiceTask> getServiceTaskList() {
        return serviceTaskList;
    }

    public void setServiceTaskList(List<ServiceTask> serviceTaskList) {
        this.serviceTaskList = serviceTaskList;
    }

    public List<ExclusiveGateway> getExclusiveGatewayList() {
        return exclusiveGatewayList;
    }

    public void setExclusiveGatewayList(List<ExclusiveGateway> exclusiveGatewayList) {
        this.exclusiveGatewayList = exclusiveGatewayList;
    }

    public List<ParallelGateway> getParallelGatewayList() {
        return parallelGatewayList;
    }

    public void setParallelGatewayList(List<ParallelGateway> parallelGatewayList) {
        this.parallelGatewayList = parallelGatewayList;
    }

    public Map<String, NodeBox> getNodeBoxList() {
        return nodeBoxList;
    }

    public void setNodeBoxList(Map<String, NodeBox> nodeBoxList) {
        this.nodeBoxList = nodeBoxList;
    }

    public List<Line> getLineList() {
        return lineList;
    }

    public void setLineList(List<Line> lineList) {
        this.lineList = lineList;
    }

    public String getFormElementId() {
        return formElementId;
    }

    public void setFormElementId(String formElementId) {
        this.formElementId = formElementId;
    }

}
