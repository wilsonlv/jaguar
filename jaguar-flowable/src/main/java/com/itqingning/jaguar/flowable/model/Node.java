package com.itqingning.jaguar.flowable.model;

import java.io.Serializable;

/**
 * Created by lvws on 2019/3/1.
 */
public class Node implements Serializable {

    public static final String STARTSTATE = "startState";
    public static final String USERTASK = "userTask";
    public static final String SERVICETASK = "serviceTask";
    public static final String ENDSTATE = "endState";
    public static final String EXCLUSIVE_GATEWAY = "exclusiveGateway";
    public static final String PARALLEL_GATEWAY = "parallelGateway";

    private String elementId;
    private String name;
    private String description;

    private NodeBox nodeBox;

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NodeBox getNodeBox() {
        return nodeBox;
    }

    public void setNodeBox(NodeBox nodeBox) {
        this.nodeBox = nodeBox;
    }
}
