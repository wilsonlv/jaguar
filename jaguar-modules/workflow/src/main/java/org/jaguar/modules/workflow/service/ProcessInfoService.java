package org.jaguar.modules.workflow.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.commons.utils.DateUtil;
import org.jaguar.core.base.BaseService;
import org.jaguar.core.exception.Assert;
import org.jaguar.core.exception.CheckedException;
import org.jaguar.modules.document.model.Document;
import org.jaguar.modules.document.service.DocumentService;
import org.jaguar.modules.workflow.enums.*;
import org.jaguar.modules.workflow.mapper.ProcessInfoMapper;
import org.jaguar.modules.workflow.model.dto.ProcessTag;
import org.jaguar.modules.workflow.model.dto.TaskDTO;
import org.jaguar.modules.workflow.model.po.*;
import org.jaguar.modules.workflow.model.vo.ActivityElement;
import org.jaguar.modules.workflow.model.vo.FlowDefinition;
import org.jaguar.modules.workflow.model.vo.ProcessView;
import org.jaguar.modules.workflow.model.vo.UserTask;
import org.jaguar.modules.workflow.util.Bpmn20Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

import static org.jaguar.modules.workflow.Constant.*;


/**
 * <p>
 * 工单信息表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
@Service
public class ProcessInfoService extends BaseService<ProcessInfo, ProcessInfoMapper> {

    @Autowired
    private IProcessUserService processUserService;
    @Autowired
    private IProcessRoleService processRoleService;
    @Autowired
    private IProcessOrgService processOrgService;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    @Autowired
    private DocumentService documentService;
    @Autowired
    private FormTemplateService formTemplateService;
    @Autowired
    private FormTemplateSheetService formTemplateSheetService;
    @Autowired
    private FormTemplateFieldService formTemplateFieldService;
    @Autowired
    private FormDataService formDataService;
    @Autowired
    private FormDataAttachService formDataAttachService;
    @Autowired
    private FlowDefinitionService flowDefinitionService;
    @Autowired
    private OperationRecordService operationRecordService;

    public static final String DEFAULT_PROCESS_PRIORITY = "3";

    public static final ThreadLocal<Boolean> REJECT = new ThreadLocal<>();


    public ProcessInfo getByProcessInstanceId(String processInstanceId) {
        return this.unique(JaguarLambdaQueryWrapper.<ProcessInfo>newInstance()
                .eq(ProcessInfo::getProcessInstanceId, processInstanceId));
    }

    /**
     * 预发起流程
     */
    @Transactional
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
        Map<String, Object> param = new HashMap<>();
        param.put(PROCESS_INITIATOR, processUser);
        param.put(PROCESS_NOW, new Date());

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
     * <p>
     * 保存时返回当前任务
     */
    @Transactional
    public Task create(String processDefinitionId, String initiator, JSONObject formDatas, boolean submit) {
        Authentication.setAuthenticatedUserId(initiator);

        IProcessUser processUser = processUserService.getByAccount(initiator);

        LocalDateTime now = LocalDateTime.now();
        //生成流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(
                processDefinitionId, new HashMap<String, Object>() {{
                    put(PROCESS_INITIATOR, processUser);
                }});

        //获取流程绑定的表单模版
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        FormTemplate formTemplate = formTemplateService.findByBpmn(bpmnModel);

        //工单基本信息
        String processNum = (String) formDatas.get(PROCESS_NUM);
        String processTitle = (String) formDatas.get(PROCESS_TITLE);
        String processPriority = (String) formDatas.get(PROCESS_PRIORITY);
        if (StringUtils.isBlank(processPriority)) {
            processPriority = DEFAULT_PROCESS_PRIORITY;
            formDatas.put(PROCESS_PRIORITY, DEFAULT_PROCESS_PRIORITY);
        }

        //生成工单信息
        ProcessInfo processInfo = new ProcessInfo();
        processInfo.setProcessDefinitionId(processDefinitionId);
        processInfo.setProcessInstanceId(processInstance.getId());
        processInfo.setFormTemplateId(formTemplate.getId());
        processInfo.setInitiator(initiator);
        processInfo.setProcessNum(processNum);
        processInfo.setTitle(processTitle);
        processInfo.setPriority(Integer.parseInt(processPriority));
        processInfo.setOrderTime(now);
        processInfo = this.insert(processInfo);

        //设置流程的基础系统变量
        runtimeService.setVariable(processInstance.getId(), PROCESS_NUM, processInfo.getProcessNum());
        runtimeService.setVariable(processInstance.getId(), PROCESS_TITLE, processInfo.getTitle());
        runtimeService.setVariable(processInstance.getId(), PROCESS_PRIORITY, processPriority);
        runtimeService.setVariable(processInstance.getId(), PROCESS_ORDER_TIME,
                DateUtil.formatDateTime(processInfo.getOrderTime()));

        //查询发起人任务
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        operationRecordService.initiate(processInfo.getId(), initiator, processInstance.getProcessDefinitionName());

        //提交发起人任务
        return this.handle(initiator, task.getId(), formDatas, submit);
    }

    /**
     * 工单提交
     */
    @Transactional
    public void handle(String currentUser, Task task, String batchNum) {
        this.handle(currentUser, task, batchNum, false, null);
    }

    /**
     * 工单提交
     */
    @Transactional
    public void handle(String currentUser, Task task, String batchNum, boolean reject, String reason) {
        Date now = new Date();

        ProcessInfo processInfo = this.getByProcessInstanceId(task.getProcessInstanceId());
        if (processInfo.getSuspend()) {
            throw new CheckedException("已挂起的工单不能被处理");
        }

        TaskAssignmentType taskAssignmentType = this.validateAssignee(currentUser, task);
        if (taskAssignmentType != TaskAssignmentType.ASSIGNEE) {
            task.setAssignee(currentUser);
            taskService.claim(task.getId(), task.getAssignee());
        }

        REJECT.set(reject);

        //工单流转
        taskService.complete(task.getId());

        if (reject) {
            operationRecordService.reject(processInfo.getId(), task, reason, batchNum);
        } else {
            operationRecordService.handle(processInfo.getId(), task, batchNum);
        }


        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInfo.getProcessInstanceId()).taskCreatedAfter(now).list();
        for (Task nextTask : tasks) {
            taskService.setPriority(nextTask.getId(), processInfo.getPriority());
            taskService.setVariableLocal(nextTask.getId(), LAST_TASK_INSTANCE_ID, task.getId());
        }
    }

    /**
     * 带有表单数据的工单提交
     * <p>
     * 保存时返回当前任务
     */
    @Transactional
    public Task handle(String currentUser, String taskId, JSONObject formDatas, boolean submit) {
        return this.handle(currentUser, taskId, formDatas, submit, false, null);
    }

    /**
     * 带有表单数据的工单提交
     * <p>
     * 保存时返回当前任务
     */
    @Transactional
    public Task handle(String currentUser, String taskId, JSONObject formDatas, boolean submit, boolean reject, String reason) {
        Date now = new Date();

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.validateId(task, "任务", taskId);

        ProcessInfo processInfo = this.getByProcessInstanceId(task.getProcessInstanceId());
        if (processInfo.getSuspend()) {
            throw new CheckedException("已挂起的工单不能被处理");
        }

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        //如果是候选人，则先认领任务
        TaskAssignmentType taskAssignmentType = this.validateAssignee(currentUser, task);
        if (taskAssignmentType != TaskAssignmentType.ASSIGNEE) {
            task.setAssignee(currentUser);
            taskService.claim(taskId, task.getAssignee());
        }

        //获取表单块权限
        UserTask userTask = Bpmn20Util.parseUserTask(bpmnModel, (org.flowable.bpmn.model.UserTask) bpmnModel.getFlowElement(task.getTaskDefinitionKey()));

        FormTemplateSheet reviewSheet = null;
        //获取任务表单块及全部组件
        if (StringUtils.isNotBlank(task.getFormKey())) {
            for (String formKey : task.getFormKey().split(",")) {
                FormTemplateSheet sheet = formTemplateSheetService.getByFormIdAndElementId(processInfo.getFormTemplateId(), formKey);
                formTemplateSheetService.fillSheetComponent(sheet);

                //保存表单数据
                formDataService.saveFormData(processInfo, userTask, sheet, formDatas, submit);

                if (!sheet.getOverride()) {
                    reviewSheet = sheet;
                }
            }
        }

        processInfo = this.updateById(processInfo);

        if (submit) {
            if (reject) {
                Assert.notNull(reason, "驳回原因");
            } else if (reviewSheet != null) {
                List<FormTemplateRow> formTemplateRows = reviewSheet.getFormTemplateRows();
                FormTemplateField reviewResult = formTemplateRows.get(0).getFormTemplateFields().get(0);
                FormTemplateField reviewRemark = formTemplateRows.get(1).getFormTemplateFields().get(0);

                if (ReviewType.REJECT.getName().equals(reviewResult.getValue())) {
                    reject = true;
                    reason = (String) reviewRemark.getValue();
                }
            }

            REJECT.set(reject);

            //工单流转
            taskService.complete(taskId);

            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInfo.getProcessInstanceId()).taskCreatedAfter(now).list();
            for (Task nextTask : tasks) {
                taskService.setPriority(nextTask.getId(), processInfo.getPriority());
                taskService.setVariableLocal(nextTask.getId(), LAST_TASK_INSTANCE_ID, taskId);

                if (reject) {
                    taskService.setVariableLocal(nextTask.getId(), PROCESS_TAGS, Collections.singletonList(
                            new ProcessTag(PROCESS_TAG_REJECT_NAME, ProcessTagType.WARNING)));
                }
            }

            if (reject) {
                operationRecordService.reject(processInfo.getId(), task, reason);
            } else {
                operationRecordService.handle(processInfo.getId(), task);
            }

            return null;
        } else {
            return task;
        }
    }

    /**
     * 实时提交表单数据（不支持扩展块的实时提交）
     */
    @Transactional
    public void submit(String currentUser, String taskId, String key, String value) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.validateId(task, "任务", taskId);

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        //如果是候选人，则先认领任务
        TaskAssignmentType taskAssignmentType = this.validateAssignee(currentUser, task);
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
        formDataService.saveFormData(processInfo, userTask, field, value);
    }

    /**
     * 附件上传
     */
    @Transactional
    public List<FormDataAttach> upload(String currentUser, String taskId, String key, List<MultipartFile> files) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.validateId(task, "任务", taskId);

        //如果是候选人，则先认领任务
        TaskAssignmentType taskAssignmentType = this.validateAssignee(currentUser, task);
        if (taskAssignmentType == TaskAssignmentType.CANDIDATE_GROUP) {
            task.setAssignee(currentUser);
            taskService.claim(taskId, currentUser);
        }

        ProcessInfo processInfo = this.getByProcessInstanceId(task.getProcessInstanceId());

        FormTemplateField field = formTemplateFieldService.getByFormIdAndFieldKey(processInfo.getFormTemplateId(), key);
        Assert.validateId(field, "表单字段", key);

        List<Document> documentList = documentService.upload(files);

        return formDataService.upload(processInfo, field, documentList);
    }

    /**
     * 删除附件
     */
    @Transactional
    public void deleteAttach(String currentUser, String taskId, Long formDataAttachId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.validateId(task, "任务", taskId);

        //如果是候选人，则先认领任务
        TaskAssignmentType taskAssignmentType = this.validateAssignee(currentUser, task);
        if (taskAssignmentType == TaskAssignmentType.CANDIDATE_GROUP) {
            task.setAssignee(currentUser);
            taskService.claim(taskId, currentUser);
        }

        formDataAttachService.delete(formDataAttachId);
    }

    /**
     * 验证处理人，如果是既不是处理人，也不是候选人，则抛出异常
     */
    public TaskAssignmentType validateAssignee(String currentUser, Task task) {
        if (currentUser.equals(task.getAssignee())) {
            return TaskAssignmentType.ASSIGNEE;
        }

        List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(task.getId());

        String orgName = processOrgService.getOrgNameByUserAccount(currentUser);
        List<String> roleNames = processRoleService.queryRoleNamesByUserAccount(currentUser);
        if (orgName != null) {
            roleNames.add(orgName);
        }

        for (IdentityLink identityLink : identityLinks) {
            if (StringUtils.isNotBlank(identityLink.getUserId())) {
                if (identityLink.getUserId().equals(currentUser)) {
                    return TaskAssignmentType.CANDIDATE_USER;
                }
            } else if (StringUtils.isNotBlank(identityLink.getGroupId())) {
                for (String roleName : roleNames) {
                    if (identityLink.getGroupId().equals(roleName)) {
                        return TaskAssignmentType.CANDIDATE_GROUP;
                    }
                }
            }
        }

        throw new AuthenticationException();
    }

    /**
     * 工单回退
     */
    @Deprecated
    @Transactional
    public void goback(String currentUser, String taskId, String reason) {
        this.goback(currentUser, taskId, reason, null);
    }

    /**
     * 工单回退
     */
    @Deprecated
    @Transactional
    public void goback(String currentUser, String taskId, String reason, String batchNum) {
        Date now = new Date();

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.validateId(task, "任务", taskId);

        ProcessInfo processInfo = this.getByProcessInstanceId(task.getProcessInstanceId());
        if (processInfo.getSuspend()) {
            throw new CheckedException("已挂起的工单不能被回退");
        }

        TaskAssignmentType taskAssignmentType = this.validateAssignee(currentUser, task);
        if (taskAssignmentType != TaskAssignmentType.ASSIGNEE) {
            task.setAssignee(currentUser);
            taskService.claim(taskId, task.getAssignee());
        }

        Object processTags = taskService.getVariable(task.getId(), PROCESS_TAGS);
        if (processTags != null) {
            throw new CheckedException("暂不支持连续回退");
        }

        String lastTaskInstanceId = (String) taskService.getVariableLocal(taskId, LAST_TASK_INSTANCE_ID);
        if (StringUtils.isBlank(lastTaskInstanceId)) {
            throw new CheckedException("当前任务不支持回退");
        }

        HistoricTaskInstance lastTaskInstance = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).taskId(lastTaskInstanceId).singleResult();

        //工单回退
        runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId())
                .moveExecutionToActivityId(task.getExecutionId(), lastTaskInstance.getTaskDefinitionKey()).changeState();

        //操作记录
        operationRecordService.goback(processInfo.getId(), task, reason, batchNum);

        //对新任务设置优先级和回退tag
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId())
                .taskDefinitionKey(lastTaskInstance.getTaskDefinitionKey()).taskCreatedAfter(now).list();
        for (Task newTask : tasks) {
            taskService.setPriority(newTask.getId(), processInfo.getPriority());
            taskService.setVariableLocal(newTask.getId(), LAST_TASK_INSTANCE_ID, taskId);
            taskService.setVariableLocal(newTask.getId(), PROCESS_TAGS, Collections.singletonList(
                    new ProcessTag(PROCESS_TAG_GO_BACK_NAME, ProcessTagType.WARNING)));
        }
    }

    /**
     * 任务改派
     */
    @Transactional
    public void reassign(String taskId, String currentUser, String reassignee, String remark) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.validateId(task, "任务", taskId);

        ProcessInfo processInfo = this.getByProcessInstanceId(task.getProcessInstanceId());
        if (processInfo.getSuspend()) {
            throw new CheckedException("已挂起的工单不能被改派");
        }

        this.validateAssignee(currentUser, task);

        IProcessUser processUser = processUserService.getByAccount(reassignee);
        Assert.validateId(processUser, "受理人", reassignee);

        taskService.setAssignee(task.getId(), reassignee);

        operationRecordService.reassign(processInfo.getId(), task, currentUser, reassignee, remark);
    }

    /**
     * 删除工单
     */
    @Transactional
    public void delete(String currentUser, Long processInfoId, String reason) {
        ProcessInfo processInfo = this.getById(processInfoId);
        Assert.validateId(processInfo, "工单信息", processInfoId);

        runtimeService.deleteProcessInstance(processInfo.getProcessInstanceId(), reason);

        this.delete(processInfoId);

        operationRecordService.cancel(processInfoId, currentUser, reason);
    }

    /**
     * 挂起工单
     */
    public void suspend(String currentUser, Long processInfoId) {
        ProcessInfo processInfo = this.getById(processInfoId);
        Assert.validateId(processInfo, "工单信息", processInfoId);
        if (processInfo.getSuspend()) {
            throw new CheckedException("该工单已经挂起");
        }

        runtimeService.suspendProcessInstanceById(processInfo.getProcessInstanceId());

        processInfo.setSuspend(true);
        this.updateById(processInfo);

        operationRecordService.suspend(processInfoId, currentUser);
    }

    /**
     * 激活工单
     */
    public void activate(String currentUser, Long processInfoId) {
        ProcessInfo processInfo = this.getById(processInfoId);
        Assert.validateId(processInfo, "工单信息", processInfoId);
        if (!processInfo.getSuspend()) {
            throw new CheckedException("非挂起状态的工单不能激活");
        }

        runtimeService.activateProcessInstanceById(processInfo.getProcessInstanceId());

        processInfo.setSuspend(false);
        this.updateById(processInfo);

        operationRecordService.activate(processInfoId, currentUser);
    }

    /**
     * 查询待办列表
     */
    public IPage<ProcessInfo> queryTasktodoList(String currentUser, IPage<ProcessInfo> page, List<String> processDefinitionName,
                                                List<String> taskName, String fuzzyTitle, String fuzzyNum, Integer priority) {
        return this.queryTasktodoList(currentUser, page, processDefinitionName, taskName, fuzzyTitle, fuzzyNum, priority, new HashMap<>());
    }

    /**
     * 查询待办列表
     */
    @Transactional
    public IPage<ProcessInfo> queryTasktodoList(String currentUser, IPage<ProcessInfo> page, List<String> processDefinitionName,
                                                List<String> taskName, String fuzzyTitle, String fuzzyNum, Integer priority, Map<String, String> taskVars) {

        int first = (int) ((page.getCurrent() - 1) * page.getSize());
        int offset = (int) page.getSize();

        String orgName = processOrgService.getOrgNameByUserAccount(currentUser);
        List<String> roleNames = processRoleService.queryRoleNamesByUserAccount(currentUser);
        if (orgName != null) {
            roleNames.add(orgName);
        }

        TaskQuery taskQuery = taskService.createTaskQuery()
                .or().taskCandidateOrAssigned(currentUser).taskCandidateGroupIn(roleNames).endOr()
                .orderByTaskPriority().asc().orderByTaskCreateTime().desc();

        if (processDefinitionName != null && processDefinitionName.size() > 0) {
            taskQuery.processDefinitionKeyIn(processDefinitionName);
        }
        if (taskName != null && taskName.size() > 0) {
            taskQuery.taskNameIn(taskName);
        }
        if (StringUtils.isNotEmpty(fuzzyTitle)) {
            taskQuery.processVariableValueLikeIgnoreCase(PROCESS_TITLE, '%' + fuzzyTitle + '%');
        }
        if (StringUtils.isNotEmpty(fuzzyNum)) {
            taskQuery.processVariableValueLikeIgnoreCase(PROCESS_NUM, '%' + fuzzyNum + '%');
        }
        if (priority != null) {
            taskQuery.processVariableValueEquals(PROCESS_PRIORITY, priority.toString());
        }
        for (Map.Entry<String, String> taskVar : taskVars.entrySet()) {
            if (StringUtils.isNotBlank(taskVar.getValue())) {
                taskQuery.taskVariableValueLike(taskVar.getKey(), '%' + taskVar.getValue() + '%');
            }
        }

        List<Task> tasks = taskQuery.listPage(first, offset);

        List<ProcessInfo> processInfos = new ArrayList<>();
        for (Task task : tasks) {
            ProcessInfo processInfo = this.getByProcessInstanceId(task.getProcessInstanceId());
            processInfo.setInitiatorEntity(processUserService.getByAccount(processInfo.getInitiator()));
            processInfos.add(processInfo);

            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setTaskDefId(task.getTaskDefinitionKey());
            taskDTO.setTaskId(task.getId());
            taskDTO.setTaskName(task.getName());
            taskDTO.setStartTime(task.getCreateTime());
            taskDTO.setDescription(task.getDescription());
            processInfo.setTaskDTO(taskDTO);

            Object processTags = taskService.getVariable(task.getId(), PROCESS_TAGS);
            if (processTags != null) {
                taskDTO.setTags((List<ProcessTag>) processTags);
            }

            if (StringUtils.isNotBlank(task.getAssignee())) {
                taskDTO.setAssignee(processUserService.getByAccount(task.getAssignee()));
            }
        }

        page.setTotal(taskQuery.count());
        page.setRecords(processInfos);
        return page;
    }

    /**
     * 查询待办数量
     */
    public int countTasktodoList(String currentUser, String processDefinitionName, List<String> taskName) {
        List<String> roleNames = processRoleService.queryRoleNamesByUserAccount(currentUser);
        String orgName = processOrgService.getOrgNameByUserAccount(currentUser);
        if (orgName != null) {
            roleNames.add(orgName);
        }

        TaskQuery taskQuery = taskService.createTaskQuery().or().taskCandidateOrAssigned(currentUser).taskCandidateGroupIn(roleNames).endOr();
        if (StringUtils.isNotBlank(processDefinitionName)) {
            taskQuery.processDefinitionKey(processDefinitionName);
        }
        if (taskName != null && taskName.size() > 0) {
            taskQuery.taskNameIn(taskName);
        }
        return ((Long) taskQuery.count()).intValue();
    }

    /**
     * 查询已办或我发起的列表
     */
    @Transactional
    public IPage<ProcessInfo> queryInstanceList(String currentUser, IPage<ProcessInfo> page, List<String> processDefinitionName,
                                                TaskStatus taskStatus, String fuzzyTitle, String fuzzyNum, Integer priority) {

        int first = (int) ((page.getCurrent() - 1) * page.getSize());
        int offset = (int) page.getSize();

        HistoricProcessInstanceQuery instanceQuery = historyService.createHistoricProcessInstanceQuery().notDeleted()
                .orderByProcessInstanceStartTime().desc();

        if (TaskStatus.I_LAUNCHED.equals(taskStatus)) {
            instanceQuery.startedBy(currentUser);
        } else if (TaskStatus.TASK_DONE.equals(taskStatus)) {
            instanceQuery.involvedUser(currentUser);
        }

        if (processDefinitionName != null && processDefinitionName.size() > 0) {
            instanceQuery.processDefinitionKeyIn(processDefinitionName);
        }
        if (StringUtils.isNotEmpty(fuzzyTitle)) {
            instanceQuery.variableValueLikeIgnoreCase(PROCESS_TITLE, '%' + fuzzyTitle + '%');
        }
        if (StringUtils.isNotEmpty(fuzzyNum)) {
            instanceQuery.variableValueLikeIgnoreCase(PROCESS_NUM, '%' + fuzzyNum + '%');
        }
        if (priority != null) {
            instanceQuery.variableValueEquals(PROCESS_PRIORITY, priority.toString());
        }

        List<HistoricProcessInstance> processInstances = instanceQuery.listPage(first, offset);

        List<ProcessInfo> processInfos = new ArrayList<>();
        for (HistoricProcessInstance processInstance : processInstances) {
            ProcessInfo processInfo = this.getByProcessInstanceId(processInstance.getId());
            processInfo.setInitiatorEntity(processUserService.getByAccount(processInfo.getInitiator()));
            processInfos.add(processInfo);

            if (processInstance.getEndTime() == null) {
                List<TaskDTO> taskDTOs = this.listCurrentTaskDTO(processInstance);
                processInfo.setCurrentTaskDTOs(taskDTOs);
            } else {
                processInfo.setEndTime(DateUtil.date2LocalDateTime(processInstance.getEndTime()));
            }
        }

        page.setTotal(instanceQuery.count());
        page.setRecords(processInfos);
        return page;
    }

    private List<TaskDTO> listCurrentTaskDTO(HistoricProcessInstance processInstance) {
        //处于查询性能考虑，每个工单只查询优秀级最高的最新的前5条数据
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId())
                .orderByTaskPriority().desc().orderByTaskCreateTime().desc()
                .listPage(0, 5);

        List<TaskDTO> taskDTOs = new ArrayList<>();
        for (Task task : tasks) {
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setStartTime(task.getCreateTime());
            taskDTO.setTaskId(task.getId());
            taskDTO.setTaskName(task.getName());
            taskDTO.setTaskDefId(task.getTaskDefinitionKey());
            taskDTOs.add(taskDTO);

            if (StringUtils.isNotBlank(task.getAssignee())) {
                IProcessUser processUser = processUserService.getByAccount(task.getAssignee());
                taskDTO.setAssignee(processUser);
            } else {
                for (IdentityLink identityLink : taskService.getIdentityLinksForTask(task.getId())) {
                    if (StringUtils.isNotBlank(identityLink.getUserId())) {
                        taskDTO.getCandidateUser().add(processUserService.getByAccount(identityLink.getUserId()));
                    }
                    if (StringUtils.isNotBlank(identityLink.getGroupId())) {
                        taskDTO.getCandidateGroup().add(identityLink.getGroupId());
                    }
                }
            }

        }
        return taskDTOs;
    }

    /**
     * 查询所有的工单
     */
    @Transactional
    public IPage<ProcessInfo> queryProcessInfo(Page<ProcessInfo> page, List<String> processDefinitionName,
                                               String fuzzyTitle, String fuzzyNum, Integer priority) {

        int first = (int) ((page.getCurrent() - 1) * page.getSize());
        int offset = (int) page.getSize();

        HistoricProcessInstanceQuery instanceQuery = historyService.createHistoricProcessInstanceQuery().notDeleted()
                .orderByProcessInstanceStartTime().desc();

        if (processDefinitionName != null && processDefinitionName.size() > 0) {
            instanceQuery.processDefinitionKeyIn(processDefinitionName);
        }
        if (StringUtils.isNotEmpty(fuzzyTitle)) {
            instanceQuery.variableValueLikeIgnoreCase(PROCESS_TITLE, '%' + fuzzyTitle + '%');
        }
        if (StringUtils.isNotEmpty(fuzzyNum)) {
            instanceQuery.variableValueLikeIgnoreCase(PROCESS_NUM, '%' + fuzzyNum + '%');
        }
        if (priority != null) {
            instanceQuery.variableValueEquals(PROCESS_PRIORITY, priority.toString());
        }

        List<HistoricProcessInstance> processInstances = instanceQuery.listPage(first, offset);

        List<ProcessInfo> processInfos = new ArrayList<>();
        for (HistoricProcessInstance processInstance : processInstances) {
            ProcessInfo processInfo = this.getByProcessInstanceId(processInstance.getId());
            processInfo.setInitiatorEntity(processUserService.getByAccount(processInfo.getInitiator()));
            processInfos.add(processInfo);

            if (processInstance.getEndTime() == null) {
                List<TaskDTO> taskDTOs = this.listCurrentTaskDTO(processInstance);
                processInfo.setCurrentTaskDTOs(taskDTOs);
            } else {
                processInfo.setEndTime(DateUtil.date2LocalDateTime(processInstance.getEndTime()));
            }
        }

        page.setTotal(instanceQuery.count());
        page.setRecords(processInfos);
        return page;
    }

    /**
     * 工单查看页面
     * 考虑到表单可能过大，所以只显示目标任务的表单块信息
     */
    @Transactional
    public ProcessInfo getViewPageByTaskDefId(Long processInfoId, String taskDefId) {
        ProcessInfo processInfo = this.getById(processInfoId);
        Assert.validateId(processInfo, "工单信息", processInfoId);

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInfo.getProcessDefinitionId());
        processInfo.setProcessDefinitionName(bpmnModel.getMainProcess().getName());

        org.flowable.bpmn.model.UserTask userTask;
        if (taskDefId == null) {
            //获取发起人任务
            FlowDefinition flowDefinition = Bpmn20Util.parseFlowBase(bpmnModel);
            userTask = Bpmn20Util.findUserTaskByName(bpmnModel, flowDefinition.getInitiateTask());
        } else {
            userTask = (org.flowable.bpmn.model.UserTask) bpmnModel.getFlowElement(taskDefId);
        }

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskDefId(userTask.getId());
        taskDTO.setTaskName(userTask.getName());
        taskDTO.setCandidateGroup(userTask.getCandidateGroups());
        processInfo.setTaskDTO(taskDTO);

        UserTask currentTask = Bpmn20Util.parseUserTask(bpmnModel, userTask);
        processInfo.setUserTask(currentTask);

        FormTemplate formTemplate = formTemplateService.getById(processInfo.getFormTemplateId());
        for (String formKey : currentTask.getFormKeys()) {
            FormTemplateSheet sheet = formTemplateSheetService.getByFormIdAndElementId(formTemplate.getId(), formKey);

            //获取目标表单块的全部组件
            formTemplateSheetService.fillSheetComponent(sheet);

            //填充表单数据
            if (sheet.getOverride()) {
                //通常情况下
                formDataService.fillFormData(processInfoId, sheet);
                formTemplate.getFormTemplateSheets().add(sheet);
            } else {
                //扩展块的情况下
                List<FormTemplateSheet> sheets = formDataService.fillFormDataForExtendSheet(processInfoId, sheet);
                formTemplate.getFormTemplateSheets().addAll(sheets);
            }
        }

        //获取已完成的任务
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInfo.getProcessInstanceId()).orderByTaskCreateTime().asc().finished().list();
        for (HistoricTaskInstance taskInstance : tasks) {
            processInfo.getHistoryTasks().put(taskInstance.getName(), taskInstance.getTaskDefinitionKey());
        }

        processInfo.setFormTemplate(formTemplate);
        return processInfo;
    }

    /**
     * 工单处理页面
     */
    @Transactional
    public ProcessInfo getHandlePageByTaskId(String currentUser, String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.validateId(task, "任务", taskId);

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        this.validateAssignee(currentUser, task);

        ProcessInfo processInfo = this.getByProcessInstanceId(task.getProcessInstanceId());
        processInfo.setProcessDefinitionName(bpmnModel.getMainProcess().getName());

        org.flowable.bpmn.model.UserTask userTask = (org.flowable.bpmn.model.UserTask) bpmnModel.getFlowElement(task.getTaskDefinitionKey());
        //获取当前任务的表单权限
        processInfo.setUserTask(Bpmn20Util.parseUserTask(bpmnModel, userTask));

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskDefId(task.getTaskDefinitionKey());
        taskDTO.setTaskId(task.getId());
        taskDTO.setTaskName(task.getName());
        taskDTO.setStartTime(task.getCreateTime());
        taskDTO.setDescription(task.getDescription());
        if (StringUtils.isNotBlank(task.getAssignee())) {
            taskDTO.setAssignee(processUserService.getByAccount(task.getAssignee()));
        }
        taskDTO.setCandidateGroup(userTask.getCandidateGroups());
        processInfo.setTaskDTO(taskDTO);

        //获取已完成的任务（同一任务可能被多次执行）
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInfo.getProcessInstanceId()).finished().list();
        for (HistoricTaskInstance taskInstance : tasks) {
            processInfo.getHistoryTasks().put(taskInstance.getName(), taskInstance.getTaskDefinitionKey());
        }

        IProcessUser processUser = processUserService.getByAccount(currentUser);
        FormTemplate formTemplate = formTemplateService.getById(processInfo.getFormTemplateId());
        processInfo.setFormTemplate(formTemplate);

        Map<String, Object> variables = runtimeService.getVariables(processInfo.getProcessInstanceId());
        variables.put(PROCESS_HANDLER, processUser);
        variables.put(PROCESS_NOW, new Date());

        //获取当前任务的的表单块
        if (StringUtils.isNotBlank(task.getFormKey())) {
            String[] formKeys = task.getFormKey().split(",");
            for (String formKey : formKeys) {
                FormTemplateSheet taskSheet = formTemplateSheetService.getByFormIdAndElementId(formTemplate.getId(), formKey);

                formTemplateSheetService.fillSheetComponent(taskSheet);

                formTemplateFieldService.resolveExpressionValue(taskSheet, variables);

                //填充表单数据
                if (taskSheet.getOverride()) {//通常情况下
                    //如果任务曾被处理过（驳回的情况下）
                    JaguarLambdaQueryWrapper<FormData> wrapper = JaguarLambdaQueryWrapper.newInstance();
                    wrapper.eq(FormData::getProcessInfoId, processInfo.getId());
                    wrapper.eq(FormData::getFormTemplateSheetId, taskSheet.getId());
                    if (formDataService.exists(wrapper)) {
                        formDataService.fillFormData(processInfo.getId(), taskSheet);
                    }
                } else {//扩展块的情况下
                    List<FormTemplateSheet> sheets = formDataService.fillFormDataForExtendSheet(processInfo.getId(), taskSheet);
                    formTemplate.getFormTemplateSheets().addAll(sheets);
                }

                formTemplate.getFormTemplateSheets().add(taskSheet);
            }
        }

        return processInfo;
    }

    /**
     * 设置工单优先级
     */
    @Transactional
    public void processPriority(Long processInfoId, String userAccount, Integer priority) {
        ProcessInfo processInfo = this.getById(processInfoId);
        Assert.validateId(processInfo, "工单信息", processInfoId);

        if (!processInfo.getInitiator().equals(userAccount)) {
            throw new AuthenticationException();
        }

        processInfo.setPriority(priority);
        this.updateById(processInfo);
        runtimeService.setVariable(processInfo.getProcessInstanceId(), PROCESS_PRIORITY, priority.toString());

        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInfo.getProcessInstanceId()).list();
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

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInfo.getProcessDefinitionId());

        //获取流程定义
        FlowDefinition flowDefinition = flowDefinitionService.getFlow(processInfo.getProcessDefinitionId());
        processView.setFlowDefinition(flowDefinition);


        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInfo.getProcessInstanceId()).list();

        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            ActivityElement activityElement = new ActivityElement();
            activityElement.setId(historicActivityInstance.getActivityId());
            activityElement.setName(historicActivityInstance.getActivityName());
            activityElement.setType(historicActivityInstance.getActivityType());

            FlowElement flowElement = bpmnModel.getFlowElement(historicActivityInstance.getActivityId());
            if (flowElement instanceof org.flowable.bpmn.model.UserTask) {

                if (StringUtils.isNotBlank(historicActivityInstance.getAssignee())) {
                    activityElement.setAssignee(processUserService.getByAccount(historicActivityInstance.getAssignee()));
                } else {
                    for (IdentityLink identityLink : taskService.getIdentityLinksForTask(historicActivityInstance.getTaskId())) {
                        if (StringUtils.isNotBlank(identityLink.getUserId())) {
                            activityElement.getCandidateUsers().add(processUserService.getByAccount(identityLink.getUserId()));
                        }
                        if (StringUtils.isNotBlank(identityLink.getGroupId())) {
                            activityElement.getCandidateGroups().add(identityLink.getGroupId());
                        }
                    }
                }

                activityElement.setStartTime(DateUtil.formatDateTime(historicActivityInstance.getStartTime()));

                if (historicActivityInstance.getEndTime() != null) {
                    activityElement.setEndTime(DateUtil.formatDateTime(historicActivityInstance.getEndTime()));
                    activityElement.setTaskStatus(TaskStatus.TASK_DONE);
                } else {
                    activityElement.setTaskStatus(TaskStatus.TASK_TODO);
                }
            }

            processView.getActivityElementList().add(activityElement);
        }

        return processView;
    }

    public TaskDTO getDoneTaskInfo(Long processInfoId, String taskName) {
        ProcessInfo processInfo = this.getById(processInfoId);

        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInfo.getProcessInstanceId()).taskName(taskName)
                .orderByHistoricTaskInstanceEndTime().desc().listPage(0, 1);

        TaskDTO taskDTO = new TaskDTO();
        if (historicTaskInstances.size() > 0) {
            HistoricTaskInstance taskInstance = historicTaskInstances.get(0);
            if (StringUtils.isNoneBlank(taskInstance.getAssignee())) {
                taskDTO.setAssignee(processUserService.getByAccount(taskInstance.getAssignee()));
            }
            taskDTO.setStartTime(taskInstance.getCreateTime());
            taskDTO.setEndTime(taskInstance.getEndTime());
            taskDTO.setTaskId(taskInstance.getId());
            taskDTO.setTaskDefId(taskInstance.getTaskDefinitionKey());
            taskDTO.setTaskName(taskName);
        }
        return taskDTO;
    }

    public boolean checkUnique(String processDefinitionName, String key, String value, Long processInfoId) {
        FormData formData = formDataService.getByKeyAndValueInProcessDefinition(processDefinitionName, key, value);
        return formData != null && (!formData.getProcessInfoId().equals(processInfoId));
    }

    @Transactional
    public OperationRecord getTaskGobackReason(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.validateId(task, "任务", taskId);

        String lastTaskInstanceId = (String) taskService.getVariableLocal(taskId, LAST_TASK_INSTANCE_ID);
        if (StringUtils.isBlank(lastTaskInstanceId)) {
            throw new CheckedException("当前任务无法查看回退原因");
        }

        return operationRecordService.unique(JaguarLambdaQueryWrapper.<OperationRecord>newInstance()
                .eq(OperationRecord::getTaskInstId, lastTaskInstanceId)
                .in(OperationRecord::getProcessOperationType, ProcessOperationType.GOBACK, ProcessOperationType.REJECT));
    }

    /**
     * 获取审核结果
     */
    public Boolean getAuditResult() {
        return !ProcessInfoService.REJECT.get();
    }
}
