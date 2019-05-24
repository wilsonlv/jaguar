package org.jaguar.modules.workflow.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lvws on 2019/3/1.
 */
@Data
public class FlowDefinition implements Serializable {

    private String id;
    @NotBlank(message = "流程名称为非空")
    private String name;
    private String initiateTask;

    private String description;
    private Integer version;

    @NotNull(message = "开始节点为非空")
    private StartState startState;
    @NotNull(message = "结束节点为非空")
    private EndState endState;

    @NotEmpty(message = "用户任务集合为非空")
    private List<UserTask> userTaskList = new ArrayList<>();
    @NotEmpty(message = "系统任务集合为非空")
    private List<ServiceTask> serviceTaskList = new ArrayList<>();
    @NotEmpty(message = "排他网关集合为非空")
    private List<ExclusiveGateway> exclusiveGatewayList = new ArrayList<>();
    @NotEmpty(message = "平行网关集合为非空")
    private List<ParallelGateway> parallelGatewayList = new ArrayList<>();
    @NotEmpty(message = "节点坐标集合非为空")
    private Map<String, NodeBox> nodeBoxList = new HashMap<>();
    @NotEmpty(message = "连线坐标集合为非空")
    private List<Line> lineList = new ArrayList<>();

    private String formElementId;

}
