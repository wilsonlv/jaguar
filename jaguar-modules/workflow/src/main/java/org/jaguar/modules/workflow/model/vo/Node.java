package org.jaguar.modules.workflow.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by lvws on 2019/3/1.
 */
@Data
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

}
