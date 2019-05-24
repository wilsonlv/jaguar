package org.jaguar.modules.workflow.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.ParallelGateway;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.api.FlowableException;
import org.jaguar.core.exception.CheckedException;
import org.jaguar.modules.workflow.enums.TaskFieldPermission;
import org.jaguar.modules.workflow.model.vo.*;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.*;

import static org.jaguar.modules.workflow.Constant.*;


/**
 * Created by lvws on 2019/3/5.
 */
public class Bpmn20Util {

    public static final ItemDefinition ITEM_DEFINITION = new ItemDefinition();

    static {
        ITEM_DEFINITION.setStructureRef("xsd:string");
    }

    /*--------------------- BPMN-->FlowDefinition ---------------------*/

    /**
     * 解析bpmn2.0
     */
    public static FlowDefinition parseFlow(BpmnModel bpmnModel) {
        FlowDefinition flowDefinition = parseFlowBase(bpmnModel);

        for (FlowElement flowElement : bpmnModel.getMainProcess().getFlowElements()) {
            if (flowElement instanceof StartEvent) {//开始节点
                StartState startState = parseStartState(bpmnModel, (StartEvent) flowElement);

                flowDefinition.setStartState(startState);
                flowDefinition.getNodeBoxList().put(startState.getName(), startState.getNodeBox());
            } else if (flowElement instanceof EndEvent) {//结束节点
                EndState endState = parseEndState(bpmnModel, (EndEvent) flowElement);

                flowDefinition.setEndState(endState);
                flowDefinition.getNodeBoxList().put(endState.getName(), endState.getNodeBox());
            } else if (flowElement instanceof UserTask) {//任务节点
                org.jaguar.modules.workflow.model.vo.UserTask userTask = parseUserTask(bpmnModel, (UserTask) flowElement);

                flowDefinition.getUserTaskList().add(userTask);
                flowDefinition.getNodeBoxList().put(userTask.getName(), userTask.getNodeBox());
            } else if (flowElement instanceof ServiceTask) {//系统任务
                org.jaguar.modules.workflow.model.vo.ServiceTask serviceTask = parseServiceTask(bpmnModel, (ServiceTask) flowElement);

                flowDefinition.getServiceTaskList().add(serviceTask);
                flowDefinition.getNodeBoxList().put(serviceTask.getName(), serviceTask.getNodeBox());
            } else if (flowElement instanceof ExclusiveGateway) {//排他网关
                org.jaguar.modules.workflow.model.vo.ExclusiveGateway exclusiveGateway = parseExclusiveGateway(bpmnModel,
                        (ExclusiveGateway) flowElement);

                flowDefinition.getExclusiveGatewayList().add(exclusiveGateway);
                flowDefinition.getNodeBoxList().put(exclusiveGateway.getName(), exclusiveGateway.getNodeBox());
            } else if (flowElement instanceof ParallelGateway) {//平行网关
                org.jaguar.modules.workflow.model.vo.ParallelGateway parallelGateway = parseParallelGateway(bpmnModel,
                        (ParallelGateway) flowElement);

                flowDefinition.getParallelGatewayList().add(parallelGateway);
                flowDefinition.getNodeBoxList().put(parallelGateway.getName(), parallelGateway.getNodeBox());
            } else if (flowElement instanceof SequenceFlow) {//连线
                Line line = parseLine(bpmnModel, (SequenceFlow) flowElement);
                flowDefinition.getLineList().add(line);
            }
        }

        return flowDefinition;
    }

    public static FlowDefinition parseFlowBase(BpmnModel bpmnModel) {
        Process process = bpmnModel.getMainProcess();

        FlowDefinition flowDefinition = new FlowDefinition();
        flowDefinition.setName(process.getName());
        flowDefinition.setDescription(process.getDocumentation());

        JSONObject processProperties = findProcessProperties(bpmnModel);
        flowDefinition.setFormElementId(processProperties.getString(PROCESS_PROPERTIES_FORM_ELEMENT_ID));
        flowDefinition.setInitiateTask(processProperties.getString(PROCESS_PROPERTIES_INITIATE_TASK));
        return flowDefinition;
    }

    /**
     * 解析流程属性
     */
    public static JSONObject findProcessProperties(BpmnModel bpmnModel) {
        String value = (String) findDataObject(bpmnModel, PROCESS_PROPERTIES).getValue();
        return JSONObject.parseObject(value);
    }

    /**
     * 解析开始节点
     */
    public static StartState parseStartState(BpmnModel bpmnModel, StartEvent startEvent) {
        StartState startState = new StartState();
        startState.setElementId(startEvent.getId());
        startState.setName(startEvent.getName());
        startState.setDescription(startEvent.getDocumentation());
        //节点坐标
        startState.setNodeBox(parseNodeBox(bpmnModel, startEvent, Node.STARTSTATE));
        return startState;
    }

    /**
     * 解析结束节点
     */
    private static EndState parseEndState(BpmnModel bpmnModel, EndEvent endEvent) {
        EndState endState = new EndState();
        endState.setElementId(endEvent.getId());
        endState.setName(endEvent.getName());
        endState.setDescription(endEvent.getDocumentation());

        endState.setNodeBox(parseNodeBox(bpmnModel, endEvent, Node.ENDSTATE));
        return endState;
    }

    /**
     * 解析任务节点
     */
    public static org.jaguar.modules.workflow.model.vo.UserTask parseUserTask(BpmnModel bpmnModel, UserTask taskElement) {
        org.jaguar.modules.workflow.model.vo.UserTask userTask = new org.jaguar.modules.workflow.model.vo.UserTask();
        userTask.setElementId(taskElement.getId());
        userTask.setName(taskElement.getName());
        userTask.setFormKeys(Arrays.asList(taskElement.getFormKey().split(",")));
        userTask.setDescription(taskElement.getDocumentation());
        userTask.setAssignee(taskElement.getAssignee());
        if (taskElement.getCandidateGroups() != null && taskElement.getCandidateGroups().size() > 0) {
            userTask.setCandidateGroup(taskElement.getCandidateGroups().get(0));
        }

        userTask.setNodeBox(parseNodeBox(bpmnModel, taskElement, Node.USERTASK));

        //表单权限
        StringDataObject dataObject = findDataObject(bpmnModel, NODE_PROPERTIES_PRE + taskElement.getName());
        Map<String, TaskFieldPermission> taskFieldPermissionMap = JSONObject.parseObject((String) dataObject.getValue(), new TypeReference<Map<String, TaskFieldPermission>>() {
        });
        userTask.setTaskFormPermissionMap(taskFieldPermissionMap);

        //任务事件
        for (FlowableListener flowableListener : taskElement.getTaskListeners()) {
            FlowableEvent flowableEvent = new FlowableEvent();
            flowableEvent.setType(flowableListener.getEvent());
            flowableEvent.setImplementation(flowableListener.getImplementation());
            flowableEvent.setImplementationType(flowableListener.getImplementationType());
            userTask.getFlowableEvents().add(flowableEvent);
        }

        if (taskElement.getLoopCharacteristics() != null) {
            MultiInstanceLoopCharacteristics multiInstance = taskElement.getLoopCharacteristics();
            userTask.setMultiInstanceAssignee(multiInstance.getInputDataItem());
            userTask.setCompletionCondition(multiInstance.getCompletionCondition());
            userTask.setSequential(multiInstance.isSequential());
        }
        return userTask;
    }

    /**
     * 解析系统任务
     */
    private static org.jaguar.modules.workflow.model.vo.ServiceTask parseServiceTask(BpmnModel bpmnModel, ServiceTask service) {
        org.jaguar.modules.workflow.model.vo.ServiceTask serviceTask = new org.jaguar.modules.workflow.model.vo.ServiceTask();
        serviceTask.setElementId(service.getId());
        serviceTask.setName(service.getName());
        serviceTask.setDescription(service.getDocumentation());

        serviceTask.setNodeBox(parseNodeBox(bpmnModel, service, Node.SERVICETASK));

        serviceTask.setImplementationType(service.getImplementationType());
        serviceTask.setImplementation(service.getImplementation());
        return serviceTask;
    }

    /**
     * 解析排他网关
     */
    private static org.jaguar.modules.workflow.model.vo.ExclusiveGateway parseExclusiveGateway(BpmnModel bpmnModel, ExclusiveGateway gateway) {
        org.jaguar.modules.workflow.model.vo.ExclusiveGateway exclusiveGateway = new org.jaguar.modules.workflow.model.vo.ExclusiveGateway();
        exclusiveGateway.setElementId(gateway.getId());
        exclusiveGateway.setName(gateway.getName());
        exclusiveGateway.setDescription(gateway.getDocumentation());

        exclusiveGateway.setNodeBox(parseNodeBox(bpmnModel, gateway, Node.EXCLUSIVE_GATEWAY));
        return exclusiveGateway;
    }

    /**
     * 解析平行网关
     */
    private static org.jaguar.modules.workflow.model.vo.ParallelGateway parseParallelGateway(BpmnModel bpmnModel, ParallelGateway gateway) {
        org.jaguar.modules.workflow.model.vo.ParallelGateway parallelGateway = new org.jaguar.modules.workflow.model.vo.ParallelGateway();
        parallelGateway.setElementId(gateway.getId());
        parallelGateway.setName(gateway.getName());
        parallelGateway.setDescription(gateway.getDocumentation());

        parallelGateway.setNodeBox(parseNodeBox(bpmnModel, gateway, Node.PARALLEL_GATEWAY));
        return parallelGateway;
    }

    /**
     * 解析连线
     */
    private static Line parseLine(BpmnModel bpmnModel, SequenceFlow sequenceFlow) {
        Line line = new Line();
        line.setElementId(sequenceFlow.getId());
        line.setName(sequenceFlow.getName());
        line.setDescription(sequenceFlow.getDocumentation());
        line.setConditionExpression(sequenceFlow.getConditionExpression());
        line.setStartBox(sequenceFlow.getSourceFlowElement().getName());
        line.setEndBox(sequenceFlow.getTargetFlowElement().getName());

        //出发节点坐标
        List<GraphicInfo> graphicInfos = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
        GraphicInfo startBoxInfo = graphicInfos.get(0);
        line.setStartBoxPortX(startBoxInfo.getX());
        line.setStartBoxPortY(startBoxInfo.getY());

        //拐点
        for (int i = 1; i < graphicInfos.size() - 1; i++) {
            GraphicInfo graphicInfo = graphicInfos.get(i);
            LineBend lineBend = new LineBend();
            lineBend.setX(graphicInfo.getX());
            lineBend.setY(graphicInfo.getY());
            line.getLineBendList().add(lineBend);
        }

        //目标节点坐标
        GraphicInfo endBoxInfo = graphicInfos.get(graphicInfos.size() - 1);
        line.setEndBoxPortX(endBoxInfo.getX());
        line.setEndBoxPortY(endBoxInfo.getY());

        //连线其他属性
        StringDataObject dataObject = findDataObject(bpmnModel, LINE_PROPERTIES_PRE + sequenceFlow.getName());
        JSONObject lineProperties = JSONObject.parseObject((String) dataObject.getValue());
        line.setIsShowEdge(lineProperties.getIntValue("isShowEdge"));
        line.setIsShowLabel(lineProperties.getIntValue("isShowLabel"));

        return line;
    }

    /**
     * 解析节点坐标
     */
    private static NodeBox parseNodeBox(BpmnModel bpmnModel, FlowElement flowElement, String type) {

        GraphicInfo graphicInfo = bpmnModel.getLocationMap().get(flowElement.getId());
        NodeBox nodeBox = new NodeBox();
        nodeBox.setName(flowElement.getName());
        nodeBox.setX(graphicInfo.getX());
        nodeBox.setY(graphicInfo.getY());
        nodeBox.setWidth(graphicInfo.getWidth());
        nodeBox.setHeight(graphicInfo.getHeight());
        nodeBox.setType(type);
        return nodeBox;
    }

    /**
     * 解析开始节点
     */
    public static StartEvent findStartEvent(BpmnModel bpmnModel) {
        for (FlowElement flowElement : bpmnModel.getMainProcess().getFlowElements()) {
            if (flowElement instanceof StartEvent) {
                return (StartEvent) flowElement;
            }
        }
        throw new FlowableException("没有找到开始节点！");
    }

    /**
     * 解析开始节点
     */
    public static UserTask findUserTaskByName(BpmnModel bpmnModel, String name) {
        for (FlowElement flowElement : bpmnModel.getMainProcess().getFlowElements()) {
            if (flowElement.getName().equals(name)) {
                return (UserTask) flowElement;
            }
        }
        throw new FlowableException("没有找到【" + name + "】任务节点！");
    }


    /*--------------------- FlowDefinition-->BPMN ---------------------*/

    /**
     * 转换为bpmn2.0
     */
    public static BpmnModel convertBPMN(FlowDefinition flowDefinition) {

        Process process = new Process();
        process.setId(flowDefinition.getName());
        process.setName(flowDefinition.getName());
        process.setDocumentation(flowDefinition.getDescription());
        process.setExecutable(true);

        Map<String, String> processProperties = new HashMap<>();
        processProperties.put(PROCESS_PROPERTIES_FORM_ELEMENT_ID, flowDefinition.getFormElementId());
        processProperties.put(PROCESS_PROPERTIES_INITIATE_TASK, flowDefinition.getInitiateTask());
        process.addFlowElement(createDataObject(PROCESS_PROPERTIES, JSONObject.toJSONString(processProperties)));

        Map<String, FlowElement> nodeElementMap = new HashMap<>();

        BpmnModel bpmnModel = new BpmnModel();
        bpmnModel.getProcesses().add(process);
        convertStartEvent(bpmnModel, process, flowDefinition, nodeElementMap);
        convertEndEvent(bpmnModel, process, flowDefinition, nodeElementMap);
        convertUserTask(bpmnModel, process, flowDefinition, nodeElementMap);
        convertServiceTask(bpmnModel, process, flowDefinition, nodeElementMap);
        convertExclusiveGateway(bpmnModel, process, flowDefinition, nodeElementMap);
        convertParallelGateway(bpmnModel, process, flowDefinition, nodeElementMap);
        converSequenceFlow(bpmnModel, process, flowDefinition, nodeElementMap);

        return bpmnModel;
    }

    /**
     * 转换为开始事件
     */
    private static StartEvent convertStartEvent(BpmnModel bpmnModel, Process process, FlowDefinition flowDefinition, Map<String, FlowElement> nodeElementMap) {

        StartState startState = flowDefinition.getStartState();
        StartEvent startEvent = new StartEvent();
        startEvent.setId(ELEMENT_ID_PRE + IdWorker.get32UUID());
        startEvent.setName(startState.getName());
        startEvent.setDocumentation(startState.getDescription());
        process.addFlowElement(startEvent);
        nodeElementMap.put(startEvent.getName(), startEvent);
        //坐标
        NodeBox startNodeBox = flowDefinition.getNodeBoxList().get(startState.getName());
        bpmnModel.getLocationMap().put(startEvent.getId(),
                new GraphicInfo(startNodeBox.getX(), startNodeBox.getY(), startNodeBox.getHeight(), startNodeBox.getWidth()));

        return startEvent;
    }

    /**
     * 转换为结束事件
     */
    private static EndEvent convertEndEvent(BpmnModel bpmnModel, Process process, FlowDefinition flowDefinition, Map<String, FlowElement> nodeElementMap) {

        EndState endState = flowDefinition.getEndState();
        EndEvent endEvent = new EndEvent();
        endEvent.setId(ELEMENT_ID_PRE + IdWorker.get32UUID());
        endEvent.setName(endState.getName());
        endEvent.setDocumentation(endState.getDescription());
        process.addFlowElement(endEvent);
        nodeElementMap.put(endEvent.getName(), endEvent);

        NodeBox endNodeBox = flowDefinition.getNodeBoxList().get(endState.getName());
        bpmnModel.getLocationMap().put(endEvent.getId(),
                new GraphicInfo(endNodeBox.getX(), endNodeBox.getY(), endNodeBox.getHeight(), endNodeBox.getWidth()));

        return endEvent;
    }

    /**
     * 转换为任务节点
     */
    private static void convertUserTask(BpmnModel bpmnModel, Process process, FlowDefinition flowDefinition, Map<String, FlowElement> nodeElementMap) {

        for (org.jaguar.modules.workflow.model.vo.UserTask task : flowDefinition.getUserTaskList()) {
            StringBuilder formKeyStr = new StringBuilder();
            for (int i = 0; i < task.getFormKeys().size(); i++) {
                if (i != 0) {
                    formKeyStr.append(",");
                }
                formKeyStr.append(task.getFormKeys().get(i));
            }

            UserTask userTask = new UserTask();
            userTask.setId(ELEMENT_ID_PRE + IdWorker.get32UUID());
            userTask.setName(task.getName());
            userTask.setAssignee(task.getAssignee());
            userTask.setFormKey(formKeyStr.toString());
            userTask.setAssignee(task.getAssignee());
            if (task.getCandidateGroup() != null) {
                userTask.setCandidateGroups(new ArrayList<String>() {{
                    add(task.getCandidateGroup());
                }});
            }
            process.addFlowElement(userTask);
            nodeElementMap.put(userTask.getName(), userTask);

            NodeBox userTaskNodeBox = flowDefinition.getNodeBoxList().get(task.getName());
            bpmnModel.getLocationMap().put(userTask.getId(), new GraphicInfo(userTaskNodeBox.getX(), userTaskNodeBox.getY(), userTaskNodeBox.getHeight(), userTaskNodeBox.getWidth()));

            process.addFlowElement(createDataObject(NODE_PROPERTIES_PRE + task.getName(), JSONObject.toJSONString(task.getTaskFormPermissionMap())));

            //事件
            for (FlowableEvent flowableEvent : task.getFlowableEvents()) {
                FlowableListener flowableListener = new FlowableListener();
                flowableListener.setEvent(flowableEvent.getType());
                flowableListener.setImplementationType(flowableEvent.getImplementationType());
                flowableListener.setImplementation(flowableEvent.getImplementation());
                userTask.getTaskListeners().add(flowableListener);
            }

            //多人处理
            if (StringUtils.isNotBlank(task.getMultiInstanceAssignee())) {
                MultiInstanceLoopCharacteristics multiInstance = new MultiInstanceLoopCharacteristics();
                multiInstance.setInputDataItem(task.getMultiInstanceAssignee());
                multiInstance.setCompletionCondition(task.getCompletionCondition());
                multiInstance.setSequential(task.getSequential());
                multiInstance.setElementVariable(ELEMENT_VARIABLE);
                userTask.setLoopCharacteristics(multiInstance);
                userTask.setAssignee(ELEMENT_VARIABLE_EXPRESSION);
            }
        }
    }

    /**
     * 转换为系统任务
     */
    private static void convertServiceTask(BpmnModel bpmnModel, Process process, FlowDefinition flowDefinition, Map<String, FlowElement> nodeElementMap) {

        for (org.jaguar.modules.workflow.model.vo.ServiceTask serviceTask : flowDefinition.getServiceTaskList()) {
            ServiceTask service = new ServiceTask();
            service.setId(ELEMENT_ID_PRE + IdWorker.get32UUID());
            service.setName(serviceTask.getName());
            service.setDocumentation(serviceTask.getDescription());
            process.addFlowElement(service);
            nodeElementMap.put(service.getName(), service);

            NodeBox endNodeBox = flowDefinition.getNodeBoxList().get(serviceTask.getName());
            bpmnModel.getLocationMap().put(service.getId(),
                    new GraphicInfo(endNodeBox.getX(), endNodeBox.getY(), endNodeBox.getHeight(), endNodeBox.getWidth()));

            service.setImplementationType(serviceTask.getImplementationType());
            service.setImplementation(serviceTask.getImplementation());
        }
    }

    /**
     * 转换为排他网关
     */
    private static void convertExclusiveGateway(BpmnModel bpmnModel, Process process, FlowDefinition flowDefinition, Map<String, FlowElement> nodeElementMap) {

        for (org.jaguar.modules.workflow.model.vo.ExclusiveGateway exclusiveGateway : flowDefinition.getExclusiveGatewayList()) {
            ExclusiveGateway gateway = new ExclusiveGateway();
            gateway.setId(ELEMENT_ID_PRE + IdWorker.get32UUID());
            gateway.setName(exclusiveGateway.getName());
            gateway.setDocumentation(exclusiveGateway.getDescription());
            process.addFlowElement(gateway);
            nodeElementMap.put(gateway.getName(), gateway);

            NodeBox endNodeBox = flowDefinition.getNodeBoxList().get(exclusiveGateway.getName());
            bpmnModel.getLocationMap().put(gateway.getId(),
                    new GraphicInfo(endNodeBox.getX(), endNodeBox.getY(), endNodeBox.getHeight(), endNodeBox.getWidth()));
        }
    }

    /**
     * 转换为平行网关
     */
    private static void convertParallelGateway(BpmnModel bpmnModel, Process process, FlowDefinition flowDefinition, Map<String, FlowElement> nodeElementMap) {

        for (org.jaguar.modules.workflow.model.vo.ParallelGateway parallelGateway : flowDefinition.getParallelGatewayList()) {
            ParallelGateway gateway = new ParallelGateway();
            gateway.setId(ELEMENT_ID_PRE + IdWorker.get32UUID());
            gateway.setName(parallelGateway.getName());
            gateway.setDocumentation(parallelGateway.getDescription());
            process.addFlowElement(gateway);
            nodeElementMap.put(gateway.getName(), gateway);

            NodeBox endNodeBox = flowDefinition.getNodeBoxList().get(parallelGateway.getName());
            bpmnModel.getLocationMap().put(gateway.getId(),
                    new GraphicInfo(endNodeBox.getX(), endNodeBox.getY(), endNodeBox.getHeight(), endNodeBox.getWidth()));
        }
    }

    /**
     * 转换为序列流
     */
    private static void converSequenceFlow(BpmnModel bpmnModel, Process process, FlowDefinition flowDefinition, Map<String, FlowElement> nodeElementMap) {

        for (Line line : flowDefinition.getLineList()) {
            SequenceFlow sequenceFlow = new SequenceFlow();
            sequenceFlow.setId(ELEMENT_ID_PRE + IdWorker.get32UUID());
            sequenceFlow.setName(line.getName());
            sequenceFlow.setSourceRef(nodeElementMap.get(line.getStartBox()).getId());
            sequenceFlow.setTargetRef(nodeElementMap.get(line.getEndBox()).getId());
            sequenceFlow.setConditionExpression(line.getConditionExpression());
            process.addFlowElement(sequenceFlow);

            ArrayList<GraphicInfo> graphicInfos = new ArrayList<>();
            graphicInfos.add(new GraphicInfo(line.getStartBoxPortX(), line.getStartBoxPortY()));//出发节点坐标
            for (LineBend lineBend : line.getLineBendList()) {
                graphicInfos.add(new GraphicInfo(lineBend.getX(), lineBend.getY()));//拐点
            }
            graphicInfos.add(new GraphicInfo(line.getEndBoxPortX(), line.getEndBoxPortY()));//目标节点坐标
            bpmnModel.getFlowLocationMap().put(sequenceFlow.getId(), graphicInfos);

            //其他属性
            JSONObject lineProperties = new JSONObject();
            lineProperties.put("isShowEdge", line.getIsShowEdge());
            lineProperties.put("isShowLabel", line.getIsShowLabel());
            process.addFlowElement(createDataObject(LINE_PROPERTIES_PRE + line.getName(), lineProperties.toJSONString()));
        }
    }

    /**
     * 查询绑定数据
     */
    private static StringDataObject findDataObject(BpmnModel bpmnModel, String name) {
        for (ValuedDataObject valuedDataObject : bpmnModel.getMainProcess().getDataObjects()) {
            if (valuedDataObject.getName().equals(name)) {
                return (StringDataObject) valuedDataObject;
            }
        }

        throw new IllegalArgumentException("data object 【" + name + "】 not found");
    }

    /**
     * 创建绑定数据
     */
    private static StringDataObject createDataObject(String name, String value) {
        StringDataObject dataObject = new StringDataObject();
        dataObject.setId(ELEMENT_ID_PRE + IdWorker.get32UUID());
        dataObject.setItemSubjectRef(ITEM_DEFINITION);
        dataObject.setName(name);
        dataObject.setValue(value);

        return dataObject;
    }


    /**
     * xml转BpmnModel
     */
    public static BpmnModel xmlToBpmnModel(InputStream inputStream) {
        XMLStreamReader xmlStreamReader;
        try {
            xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
        } catch (XMLStreamException e) {
            throw new CheckedException(e);
        }

        BpmnXMLConverter converter = new BpmnXMLConverter();
        return converter.convertToBpmnModel(xmlStreamReader);
    }
}
