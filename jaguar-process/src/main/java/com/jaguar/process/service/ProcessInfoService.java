package com.jaguar.process.service;

import com.alibaba.fastjson.JSONObject;
import com.jaguar.core.base.BaseService;
import com.jaguar.core.exception.AuthenticationException;
import com.jaguar.core.util.Assert;
import com.jaguar.core.util.DateUtil;
import com.jaguar.core.util.InstanceUtil;
import com.jaguar.core.util.UUIDUtil;
import com.jaguar.process.Constant;
import com.jaguar.process.enums.ProcessOperationType;
import com.jaguar.process.enums.TaskAssignmentType;
import com.jaguar.process.enums.TaskStatus;
import com.jaguar.process.mapper.ProcessInfoMapper;
import com.jaguar.process.model.dto.TaskDTO;
import com.jaguar.process.model.po.*;
import com.jaguar.process.model.vo.ActivityElement;
import com.jaguar.process.model.vo.FlowDefinition;
import com.jaguar.process.model.vo.ProcessView;
import com.jaguar.process.model.vo.UserTask;
import com.jaguar.process.util.Bpmn20Util;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工单信息表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
@Service
@CacheConfig(cacheNames = "ProcessInfo")
public class ProcessInfoService extends BaseService<ProcessInfo, ProcessInfoMapper> {

    @Autowired
    private IProcessUserService processUserService;
    @Autowired
    private IProcessRoleService processRoleService;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    @Autowired
    private FormTemplateService formTemplateService;
    @Autowired
    private FormTemplateSheetService formTemplateSheetService;
    @Autowired
    private FormTemplateFieldService formTemplateFieldService;
    @Autowired
    private FormDataService formDataService;
    @Autowired
    private FlowDefinitionService flowDefinitionService;
    @Autowired
    private ProcessOperationRecordService processOperationRecordService;


    public ProcessInfo getByProcessInstanceId(String processInstanceId) {
        Map<String, Object> param = InstanceUtil.newHashMap();
        param.put("processInstanceId", processInstanceId);
        return this.unique(param);
    }

    /**
     * 预发起流程
     */
    public ProcessInfo preCreate(String initiator, String processDefinitionKey) {
        IProcessUser processUser = processUserService.getByAccount(initiator);

        //查询流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey).latestVersion().singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());

        //获取发起人任务及表单权限
        FlowDefinition flowDefinition = Bpmn20Util.parseFlowBase(bpmnModel);
        UserTask userTask = Bpmn20Util.parseUserTask(bpmnModel, Bpmn20Util.findUserTaskByName(bpmnModel, flowDefinition.getInitiateTask()));

        //表单字段的环境变量
        Map<String, Object> param = InstanceUtil.newHashMap();
        param.put(Constant.PROCESS_INITIATOR, processUser);
        param.put(Constant.PROCESS_NOW, new Date());

        //查询对应的表单块
        FormTemplate formTemplate = formTemplateService.findByBpmn(bpmnModel);
        for (String formKey : userTask.getFormKeys()) {
            FormTemplateSheet sheet = formTemplateSheetService.getByFormIdAndElementId(formTemplate.getId(), formKey);
            formTemplateSheetService.fillSheetComponent(sheet);
            formTemplate.getFormTemplateSheets().add(sheet);

            //解析表单字段的默认值
            formTemplateFieldService.resolveExpressionValue(sheet, param);
        }

        ProcessInfo processInfo = new ProcessInfo();
        processInfo.setProcessDefinitionId(processDefinition.getId());
        processInfo.setProcessDefinitionName(processDefinition.getKey());
        processInfo.setFormTemplate(formTemplate);
        processInfo.setUserTask(userTask);

        return processInfo;
    }

    /**
     * 发起流程
     */
    @Transactional
    public void create(String processDefinitionId, String initiator, JSONObject formDatas, Boolean submit) {
        Authentication.setAuthenticatedUserId(initiator);

        IProcessUser processUser = processUserService.getByAccount(initiator);

        //生成流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(
                processDefinitionId, InstanceUtil.newHashMap(Constant.PROCESS_INITIATOR, processUser));

        //获取流程绑定的表单模版
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        FormTemplate formTemplate = formTemplateService.findByBpmn(bpmnModel);

        //工单基本信息
        String processNum = (String) formDatas.get(Constant.PROCESS_NUM);
        String processTitle = (String) formDatas.get(Constant.PROCESS_TITLE);
        String processPriority = (String) formDatas.get(Constant.PROCESS_PRIORITY);
        Integer priority = processPriority != null ? Integer.parseInt(processPriority) : null;

        //生成工单信息
        ProcessInfo processInfo = new ProcessInfo();
        processInfo.setProcessDefinitionId(processDefinitionId);
        processInfo.setProcessInstanceId(processInstance.getId());
        processInfo.setFormTemplateId(formTemplate.getId());
        processInfo.setInitiator(initiator);
        processInfo.setProcessNum(StringUtils.isBlank(processNum) ? UUIDUtil.getId() : processNum);
        processInfo.setTitle(processTitle);
        processInfo.setPriority(priority);
        processInfo.setOrderTime(processInstance.getStartTime());
        processInfo = this.update(processInfo);

        //设置流程的基础系统变量
        runtimeService.setVariable(processInstance.getId(), Constant.PROCESS_NUM, processInfo.getProcessNum());
        runtimeService.setVariable(processInstance.getId(), Constant.PROCESS_TITLE, processInfo.getTitle());
        runtimeService.setVariable(processInstance.getId(), Constant.PROCESS_PRIORITY, processInfo.getPriority());
        runtimeService.setVariable(processInstance.getId(), Constant.PROCESS_ORDER_TIME,
                DateUtil.format(processInfo.getOrderTime(), DateUtil.DatePattern.YYYY_MM_DD_HH_MM_SS));

        //查询发起人任务
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        processOperationRecordService.initiate(processInfo, initiator, processInstance.getProcessDefinitionName());

        //提交发起人任务
        this.handle(initiator, task.getId(), formDatas, submit);
    }

    /**
     * 工单提交
     */
    @Transactional
    public void handle(String currentUser, String taskId, JSONObject formDatas, Boolean submit) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.validateId(task, "任务", taskId);

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        //如果是候选人，则先认领任务
        TaskAssignmentType taskAssignmentType = this.validateAssignee(currentUser, task, bpmnModel);
        if (taskAssignmentType == TaskAssignmentType.CANDIDATE_GROUP) {
            task.setAssignee(currentUser);
            taskService.claim(taskId, task.getAssignee());
        }

        ProcessInfo processInfo = this.getByProcessInstanceId(task.getProcessInstanceId());

        //获取表单块权限
        UserTask userTask = Bpmn20Util.parseUserTask(bpmnModel, (org.flowable.bpmn.model.UserTask) bpmnModel.getFlowElement(task.getTaskDefinitionKey()));

        //获取任务表单块及全部组件
        for (String formKey : task.getFormKey().split(",")) {
            FormTemplateSheet sheet = formTemplateSheetService.getByFormIdAndElementId(processInfo.getFormTemplateId(), formKey);
            formTemplateSheetService.fillSheetComponent(sheet);

            //保存表单数据
            formDataService.saveFormData(processInfo.getId(), processInfo.getProcessInstanceId(), userTask, sheet, formDatas);
        }

        if (submit) {
            //工单流转
            taskService.complete(taskId);

            this.taskPriority(processInfo.getProcessInstanceId(), processInfo.getPriority());

            processOperationRecordService.operate(processInfo, task, ProcessOperationType.HANDLE);
        }
    }

    /**
     * 实时提交表单数据
     */
    @Transactional
    public void submit(String currentUser, String taskId, String key, String value) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.validateId(task, "任务", taskId);

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        //如果是候选人，则先认领任务
        TaskAssignmentType taskAssignmentType = this.validateAssignee(currentUser, task, bpmnModel);
        if (taskAssignmentType == TaskAssignmentType.CANDIDATE_GROUP) {
            task.setAssignee(currentUser);
            taskService.claim(taskId, currentUser);
        }

        ProcessInfo processInfo = this.getByProcessInstanceId(task.getProcessInstanceId());

        //获取表单块权限
        UserTask userTask = Bpmn20Util.parseUserTask(bpmnModel, (org.flowable.bpmn.model.UserTask) bpmnModel.getFlowElement(task.getTaskDefinitionKey()));

        FormTemplateField field = formTemplateFieldService.getByFormIdAndFieldKey(processInfo.getFormTemplateId(), key);
        Assert.validateId(field, "表单字段", key);

        //保存表单数据
        formDataService.saveFormData(processInfo.getId(), processInfo.getProcessInstanceId(), userTask, field, value);

    }

    /**
     * 验证处理人，如果是既不是处理人，也不是候选人，则抛出异常
     */
    private TaskAssignmentType validateAssignee(String currentUser, Task task, BpmnModel bpmnModel) {
        if (currentUser.equals(task.getAssignee())) {
            return TaskAssignmentType.ASSIGNEE;
        }

        org.flowable.bpmn.model.UserTask userTask = (org.flowable.bpmn.model.UserTask)
                bpmnModel.getFlowElement(task.getTaskDefinitionKey());

        ProcessRole processRole = processRoleService.getByUserAccount(currentUser);

        for (String role : userTask.getCandidateGroups()) {
            if (role.equals(processRole.getRoleName())) {
                return TaskAssignmentType.CANDIDATE_GROUP;
            }
        }

        throw new AuthenticationException();
    }

    /**
     * 工单回退
     */
    @Transactional
    public void goback(Long id, String currentUser) {
        ProcessInfo processInfo = this.getById(id);
        Assert.validateId(processInfo, "工单信息", id);

        String processInstanceId = processInfo.getProcessInstanceId();

        ActivityInstance currentActivity = runtimeService.createActivityInstanceQuery()
                .processInstanceId(processInstanceId).unfinished().singleResult();

        if (!currentUser.equals(currentActivity.getAssignee())) {
            throw new AuthenticationException();
        }

        Task task = taskService.createTaskQuery().processInstanceId(processInfo.getProcessInstanceId()).singleResult();

        HistoricActivityInstance lastActivity = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).activityType(BpmnXMLConstants.ELEMENT_TASK_USER).finished()
                .orderByHistoricActivityInstanceEndTime().desc().listPage(0, 1).get(0);

        runtimeService.createChangeActivityStateBuilder().processInstanceId(processInstanceId)
                .moveActivityIdTo(currentActivity.getActivityId(), lastActivity.getActivityId()).changeState();

        processOperationRecordService.operate(processInfo, task, ProcessOperationType.GOBACK);
    }

    /**
     * 工单改派
     */
    @Transactional
    public void reassign(Long id, String currentUser, String assignee) {
        ProcessInfo processInfo = this.getById(id);
        Assert.validateId(processInfo, "工单信息", id);

        Task task = taskService.createTaskQuery().processInstanceId(processInfo.getProcessInstanceId()).singleResult();
        if (!currentUser.equals(task.getAssignee())) {
            throw new AuthenticationException();
        }

        IProcessUser processUser = processUserService.getByAccount(assignee);
        Assert.validateId(processUser, "受让人", assignee);

        taskService.setAssignee(task.getId(), assignee);

        processOperationRecordService.reassign(processInfo, task, assignee);
    }

    /**
     * 删除订单
     */
    @Transactional
    public void delete(String currentUser, Long processInfoId, String reason) {
        ProcessInfo processInfo = this.getById(processInfoId);
        Assert.validateId(processInfo, "工单信息", processInfoId);

        if (!processInfo.getInitiator().equals(currentUser)) {
            throw new AuthenticationException();
        }

        runtimeService.deleteProcessInstance(processInfo.getProcessInstanceId(), reason);

        this.del(processInfoId);

        processOperationRecordService.cancel(processInfo);
    }

    /**
     * 查询待办列表
     */
    public List<ProcessInfo> queryTasktodoList(String currentUser, int first, int offset) {
        ProcessRole processRole = processRoleService.getByUserAccount(currentUser);

        TaskQuery taskQuery = taskService.createTaskQuery()
                .or().taskCandidateOrAssigned(currentUser).taskCandidateGroup(processRole.getRoleName()).endOr();
        taskQuery = taskQuery.orderByTaskPriority().asc().orderByTaskCreateTime().desc();
        List<Task> tasks = taskQuery.listPage(first, offset);

        List<ProcessInfo> processInfos = InstanceUtil.newArrayList();
        for (Task task : tasks) {
            ProcessInfo processInfo = this.getByProcessInstanceId(task.getProcessInstanceId());
            processInfos.add(processInfo);

            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setTaskDefId(task.getTaskDefinitionKey());
            taskDTO.setTaskId(task.getId());
            taskDTO.setTaskName(task.getName());
            taskDTO.setAssignee(task.getAssignee());
            taskDTO.setStartTime(task.getCreateTime());
            processInfo.setTaskDTO(taskDTO);
        }
        return processInfos;
    }

    /**
     * 查询待办数量
     */
    public long countTasktodoList(String currentUser) {
        ProcessRole processRole = processRoleService.getByUserAccount(currentUser);
        return taskService.createTaskQuery().or().taskAssignee(currentUser)
                .taskCandidateGroup(processRole.getRoleName()).endOr().count();
    }

    /**
     * 查询已办列表
     */
    public List<ProcessInfo> queryTaskdoneList(String currentUser, int first, int offset) {
        List<HistoricProcessInstance> processInstances = historyService.createHistoricProcessInstanceQuery().notDeleted()
                .involvedUser(currentUser).orderByProcessInstanceStartTime().desc().listPage(first, offset);

        List<ProcessInfo> processInfos = InstanceUtil.newArrayList();
        for (HistoricProcessInstance processInstance : processInstances) {
            ProcessInfo processInfo = this.getByProcessInstanceId(processInstance.getId());
            processInfos.add(processInfo);

            if (processInstance.getEndTime() == null) {
                TaskDTO taskDTO = this.queryTaskDoneDTO(processInstance);
                processInfo.setTaskDTO(taskDTO);
            } else {
                processInfo.setEndTime(processInstance.getEndTime());
            }
        }
        return processInfos;
    }

    /**
     * 查询我发起的列表
     */
    public List<ProcessInfo> queryIlaunchedList(String currentUser, int first, int offset) {
        List<HistoricProcessInstance> processInstances = historyService.createHistoricProcessInstanceQuery().notDeleted()
                .startedBy(currentUser).orderByProcessInstanceStartTime().desc().listPage(first, offset);

        List<ProcessInfo> processInfos = InstanceUtil.newArrayList();
        for (HistoricProcessInstance processInstance : processInstances) {
            ProcessInfo processInfo = this.getByProcessInstanceId(processInstance.getId());
            processInfos.add(processInfo);

            if (processInstance.getEndTime() == null) {
                TaskDTO taskDTO = this.queryTaskDoneDTO(processInstance);
                processInfo.setTaskDTO(taskDTO);
            } else {
                processInfo.setEndTime(processInstance.getEndTime());
            }
        }
        return processInfos;
    }

    private TaskDTO queryTaskDoneDTO(HistoricProcessInstance processInstance) {
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        Task task = tasks.get(0);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskName(task.getName());
        taskDTO.setStartTime(task.getCreateTime());
        taskDTO.setTaskDefId(task.getTaskDefinitionKey());

        if (tasks.size() == 1) {
            taskDTO.setTaskId(task.getId());
            if (task.getAssignee() != null) {
                taskDTO.setAssignee(task.getAssignee());
            } else {
                BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
                org.flowable.bpmn.model.UserTask userTask = (org.flowable.bpmn.model.UserTask)
                        bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

                taskDTO.setAssignee(userTask.getCandidateGroups().get(0));
            }
        } else {
            StringBuilder assignees = new StringBuilder();
            for (Task item : tasks) {
                assignees.append(item.getAssignee()).append(",");
            }
            taskDTO.setAssignee(assignees.deleteCharAt(assignees.length() - 1).toString());
        }
        return taskDTO;
    }

    /**
     * 工单查看页面
     * 考虑到表单可能过大，所以只显示目标任务的表单块信息
     */
    public ProcessInfo getViewPageByTaskId(Long processInfoId, String taskId) {
        ProcessInfo processInfo = this.getById(processInfoId);
        Assert.validateId(processInfo, "流程信息", processInfoId);

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInfo.getProcessDefinitionId());

        org.flowable.bpmn.model.UserTask userTask;
        if (taskId == null) {
            //获取发起人任务
            FlowDefinition flowDefinition = Bpmn20Util.parseFlowBase(bpmnModel);
            userTask = Bpmn20Util.findUserTaskByName(bpmnModel, flowDefinition.getInitiateTask());
        } else {
            userTask = (org.flowable.bpmn.model.UserTask) bpmnModel.getFlowElement(taskId);
        }

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskDefId(userTask.getId());
        taskDTO.setTaskName(userTask.getName());
        processInfo.setTaskDTO(taskDTO);

        UserTask currentTask = Bpmn20Util.parseUserTask(bpmnModel, userTask);

        FormTemplate formTemplate = formTemplateService.getById(processInfo.getFormTemplateId());
        for (String formKey : currentTask.getFormKeys()) {
            FormTemplateSheet sheet = formTemplateSheetService.getByFormIdAndElementId(formTemplate.getId(), formKey);
            formTemplate.getFormTemplateSheets().add(sheet);

            //获取目标表单块的全部组件
            formTemplateSheetService.fillSheetComponent(sheet);

            //填充表单数据
            formDataService.fillFormData(processInfoId, sheet);
        }

        //获取已完成的任务
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInfo.getProcessInstanceId()).orderByTaskCreateTime().asc().finished().list();
        for (HistoricTaskInstance taskInstance : tasks) {
            processInfo.getTaskDTOS().put(taskInstance.getName(), taskInstance.getTaskDefinitionKey());
        }

        processInfo.setFormTemplate(formTemplate);
        return processInfo;
    }

    /**
     * 工单处理页面
     */
    public ProcessInfo getHandlePageByTaskId(String currentUser, String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.validateId(task, "任务", taskId);

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        this.validateAssignee(currentUser, task, bpmnModel);

        ProcessInfo processInfo = this.getByProcessInstanceId(task.getProcessInstanceId());

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskDefId(task.getTaskDefinitionKey());
        taskDTO.setTaskId(task.getId());
        taskDTO.setTaskName(task.getName());
        taskDTO.setAssignee(task.getAssignee());
        taskDTO.setStartTime(task.getCreateTime());
        processInfo.setTaskDTO(taskDTO);

        //获取已完成的任务（同一任务可能被多次执行）
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInfo.getProcessInstanceId()).finished().list();
        for (HistoricTaskInstance taskInstance : tasks) {
            processInfo.getTaskDTOS().put(taskInstance.getName(), taskInstance.getTaskDefinitionKey());
        }

        IProcessUser processUser = processUserService.getByAccount(currentUser);
        FormTemplate formTemplate = formTemplateService.getById(processInfo.getFormTemplateId());

        Map<String, Object> variables = runtimeService.getVariables(processInfo.getProcessInstanceId());
        variables.put(Constant.PROCESS_HANDLER, processUser);
        variables.put(Constant.PROCESS_NOW, new Date());

        //获取当前任务的的表单块
        String[] formKeys = task.getFormKey().split(",");
        for (String formKey : formKeys) {
            FormTemplateSheet taskSheet = formTemplateSheetService.getByFormIdAndElementId(formTemplate.getId(), formKey);
            formTemplateSheetService.fillSheetComponent(taskSheet);
            formTemplateFieldService.resolveExpressionValue(taskSheet, variables);
            formTemplate.getFormTemplateSheets().add(taskSheet);

            //如果任务曾被处理过（驳回的情况下）
            Map<String, Object> param = InstanceUtil.newHashMap();
            param.put("processInfoId", processInfo.getId());
            param.put("formTemplateSheetId", taskSheet.getId());
            if (formDataService.exists(param)) {
                formDataService.fillFormData(processInfo.getId(), taskSheet);
            }
        }

        //获取当前任务的表单权限
        UserTask userTask = Bpmn20Util.parseUserTask(bpmnModel, (org.flowable.bpmn.model.UserTask) bpmnModel.getFlowElement(task.getTaskDefinitionKey()));
        processInfo.setUserTask(userTask);

        processInfo.setFormTemplate(formTemplate);
        return processInfo;
    }

    /**
     * 设置任务优先级
     */
    private void taskPriority(String processInstanceId, Integer priority) {
        if (priority == null) {
            return;
        }

        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        for (Task task : tasks) {
            taskService.setPriority(task.getId(), priority);
        }
    }

    /**
     * 获取工单流转图
     */
    public ProcessView getProcessViewByProcessInfoId(Long id) {
        ProcessInfo processInfo = this.getById(id);
        Assert.validateId(processInfo, "工单信息", id);

        ProcessView processView = new ProcessView();

        //获取流程定义
        FlowDefinition flowDefinition = flowDefinitionService.getFlow(processInfo.getProcessDefinitionId());
        processView.setFlowDefinition(flowDefinition);

        //获取进行中的任务
        List<ActivityInstance> activityInstances = runtimeService.createActivityInstanceQuery()
                .processInstanceId(processInfo.getProcessInstanceId()).list();
        for (ActivityInstance activityInstance : activityInstances) {
            ActivityElement activityElement = new ActivityElement();
            activityElement.setId(activityInstance.getActivityId());
            activityElement.setName(activityInstance.getActivityName());
            activityElement.setType(activityInstance.getActivityType());
            activityElement.setAssignee(activityInstance.getAssignee());
            activityElement.setStartTime(DateUtil.format(activityInstance.getStartTime(), DateUtil.DatePattern.YYYY_MM_DD_HH_MM_SS));

            if (activityInstance.getEndTime() != null) {
                activityElement.setEndTime(DateUtil.format(activityInstance.getEndTime(), DateUtil.DatePattern.YYYY_MM_DD_HH_MM_SS));
                activityElement.setTaskStatus(TaskStatus.TASK_DONE);
            } else {
                activityElement.setTaskStatus(TaskStatus.TASK_TODO);
            }

            if (activityInstance.getActivityType().equals(BpmnXMLConstants.ELEMENT_EVENT_START)) {
                activityElement.setAssignee(processInfo.getInitiator());
            } else {
                activityElement.setAssignee(activityInstance.getAssignee());
            }

            processView.getActivityElementList().add(activityElement);
        }

        return processView;
    }

}
