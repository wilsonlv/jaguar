package com.jaguar.flowable.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvws on 2019/3/14.
 */
public class ProcessView implements Serializable {

    private FlowDefinition flowDefinition;
    private List<ActivityElement> activityElementList = new ArrayList<>();

    public FlowDefinition getFlowDefinition() {
        return flowDefinition;
    }

    public void setFlowDefinition(FlowDefinition flowDefinition) {
        this.flowDefinition = flowDefinition;
    }

    public List<ActivityElement> getActivityElementList() {
        return activityElementList;
    }

    public void setActivityElementList(List<ActivityElement> activityElementList) {
        this.activityElementList = activityElementList;
    }
}
