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
    @NotEmpty(message = "流程名称为非空")
    private String name;
    private String initiateTask;

    private String description;
    private Integer version;

    @NotNull(message = "开始节点为非空")
    private StartState startState;
    @NotNull(message = "结束节点为非空")
    private EndState endState;

    @NotNull(message = "任务节点集合")
    private List<UserTask> userTaskList = new ArrayList<>();
    @NotNull(message = "服务节点集合")
    private List<ServiceTask> serviceTaskList = new ArrayList<>();
    @NotNull(message = "等待节点集合")
    private List<ReceiveTask> receiveTaskList = new ArrayList<>();
    @NotNull(message = "决策节点集合")
    private List<ExclusiveGateway> exclusiveGatewayList = new ArrayList<>();
    @NotNull(message = "平行网管集合")
    private List<ParallelGateway> parallelGatewayList = new ArrayList<>();
    @NotNull(message = "包容网管集合")
    private List<InclusiveGateway> inclusiveGatewayList = new ArrayList<>();

    @NotNull(message = "节点坐标集合")
    private Map<String, NodeBox> nodeBoxList = new HashMap<>();
    @NotNull(message = "连线坐标集合")
    private List<Line> lineList = new ArrayList<>();

    @NotBlank(message = "表单名称")
    private String formName;
    @NotBlank(message = "表单元素ID")
    private String formElementId;

}
