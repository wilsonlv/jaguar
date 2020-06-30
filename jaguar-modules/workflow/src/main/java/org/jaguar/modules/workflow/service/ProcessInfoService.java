package org.jaguar.modules.workflow.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.beanutils.BeanUtils;
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
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.commons.utils.DateUtil;
import org.jaguar.core.base.BaseService;
import org.jaguar.core.exception.Assert;
import org.jaguar.core.exception.CheckedException;
import org.jaguar.modules.document.model.Document;
import org.jaguar.modules.document.service.DocumentService;
import org.jaguar.modules.workflow.enums.ProcessTagType;
import org.jaguar.modules.workflow.enums.ReviewType;
import org.jaguar.modules.workflow.enums.TaskAssignmentType;
import org.jaguar.modules.workflow.enums.TaskStatus;
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

import java.lang.reflect.InvocationTargetException;
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
    private IProcessUserService processUserService;
    @Autowired
    private IProcessRoleService processRoleService;
    @Autowired
    private IProcessOrgService processOrgService;

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
    @Autowired
    private ProcessRemarkService processRemarkService;

    public static final String DEFAULjaguar_modules_workflow_PRIORITY = "3";

    public static final ThreadLocal<Boolean> REJECT = new ThreadLocal<>();


    public ProcessInfo getByProcessInstanceId(String processInstanceId) {
        return this.unique(JaguarLambdaQueryWrapper.<ProcessInfo>newInstance()
                .eq(ProcessInfo::getProcessInstanceId, processInstanceId));
    }

    public Task getTaskById(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.validateId(task, "任务", taskId);
        return task;
    }

    public ProcessInfo validateProcessStatus(String processInstanceId) {
        ProcessInfo processInfo = this.getByProcessInstanceId(processInstanceId);
        if (processInfo.getSuspend()) {
            throw new CheckedException("已挂起的工单不能被处理");
        }
        return processInfo;
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
     * 如果是候选人，则先认领任务
     */
    public void validateAssigneeAndClaim(String currentUser, Task task) {
        TaskAssignmentType taskAssignmentType = this.validateAssignee(currentUser, task);
        if (taskAssignmentType == TaskAssignmentType.CANDIDATE_GROUP) {
            task.setAssignee(currentUser);
            taskService.claim(task.getId(), currentUser);
        }
    }

    /**
     * 验证流程
     */
    public ProcessInfo validate(String currentUser, String taskId) {
        Task task = this.getTaskById(taskId);
        ProcessInfo processInfo = this.validateProcessStatus(task.getProcessInstanceId());
        this.validateAssigneeAndClaim(currentUser, task);

        processInfo.setTask(task);
        return processInfo;
    }

    /**
     * 获取指定任务实例的任务定义
     *
     * @param task 任务实例
     * @return 任务定义
     */
    private UserTask getTaskDefByTaskInst(Task task) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        return Bpmn20Util.parseUserTask(bpmnModel, (org.flowable.bpmn.model.UserTask) bpmnModel.getFlowElement(task.getTaskDefinitionKey()));
    }

    /**
     * 预发起流程
     */
    @Transactional
    public ProcessInfo preCreate(String initiator, String processDefinitionKey) {
        //查询流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey).latestVersion().singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());

        //获取发起人任务及表单权限
        FlowDefinition flowDefinition = Bpmn20Util.parseFlowBase(bpmnModel);
        UserTask userTask = Bpmn20Util.parseUserTask(bpmnModel, Bpmn20Util.findUserTaskByName(bpmnModel, flowDefinition.getInitiateTask()));

        //表单字段的环境变量
        IProcessUser processUser = processUserService.getByAccount(initiator);
        Map<String, String> initiatorInfo;
        try {
            initiatorInfo = BeanUtils.describe(processUser);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new CheckedException(e);
        }

        String orgName = processOrgService.getOrgNameByUserAccount(initiator);
        initiatorInfo.put("orgName", orgName);

        Map<String, Object> param = new HashMap<>();
        param.put(PROCESS_INITIATOR, initiatorInfo);
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
        Map<String, String> initiatorInfo;
        try {
            initiatorInfo = BeanUtils.describe(processUser);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new CheckedException(e);
        }

        String orgName = processOrgService.getOrgNameByUserAccount(initiator);
        initiatorInfo.put("orgName", orgName);

        //生成流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(
                processDefinitionId, new HashMap<String, Object>() {{
                    put(PROCESS_INITIATOR, initiatorInfo);
                }});

        //获取流程绑定的表单模版
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        FormTemplate formTemplate = formTemplateService.findByBpmn(bpmnModel);

        //工单基本信息
        String processNum = (String) formDatas.get(PROCESS_NUM);
        String processTitle = (String) formDatas.get(PROCESS_TITLE);
        String processPriority = (String) formDatas.get(PROCESS_PRIORITY);
        if (StringUtils.isBlank(processPriority)) {
            processPriority = DEFAULjaguar_modules_workflow_PRIORITY;
            formDatas.put(PROCESS_PRIORITY, DEFAULjaguar_modules_workflow_PRIORITY);
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
        processInfo.setOrderTime(DateUtil.date2LocalDateTime(processInstance.getStartTime()));
        processInfo = this.insert(processInfo);

        //设置流程的基础系统变量
        runtimeService.setVariable(processInstance.getId(), PROCESS_NUM, processInfo.getProcessNum());
        runtimeService.setVariable(processInstance.getId(), PROCESS_TITLE, processInfo.getTitle());
        runtimeService.setVariable(processInstance.getId(), PROCESS_PRIORITY, processPriority);
        runtimeService.setVariable(processInstance.getId(), PROCESS_ORDER_TIME, DateUtil.formatDateTime(processInfo.getOrderTime()));

        //查询发起人任务
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        //发起操作
        operationRecordService.initiate(processInfo.getId(), initiator, processInstance.getProcessDefinitionName());

        //提交发起人任务
        return this.handle(initiator, task.getId(), formDatas, submit, false, null);
    }

    /**
     * 带有表单数据的工单保存
     * <p>
     * 返回当前任务
     */
    @Transactional
    public Task save(String currentUser, String taskId, JSONObject formDatas) {
        return this.handle(currentUser, taskId, formDatas, false, false, null);
    }

    /**
     * 工单提交
     */
    @Transactional
    public Task submit(String currentUser, String taskId) {
        return this.submit(currentUser, taskId, new JSONObject());
    }

    /**
     * 带有表单数据的工单提交
     */
    @Transactional
    public Task submit(String currentUser, String taskId, JSONObject formDatas) {
        return this.handle(currentUser, taskId, formDatas, true, false, null);
    }


    /**
     * 工单驳回
     */
    @Transactional
    public Task reject(String currentUser, String taskId, String reason) {
        return this.handle(currentUser, taskId, new JSONObject(), true, true, reason);
    }

    /**
     * 带有表单数据的工单提交
     * <p>
     * 保存时返回当前任务
     */
    @Transactional
    public Task handle(String currentUser, String taskId, JSONObject formDatas, boolean submit, boolean reject, String reason) {
        Date now = new Date();

        Task task = this.getTaskById(taskId);

        this.validateAssigneeAndClaim(currentUser, task);
        ProcessInfo processInfo = this.validateProcessStatus(task.getProcessInstanceId());
        UserTask userTask = getTaskDefByTaskInst(task);

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
     * 批量提交工单
     */
    @Transactional
    public void batchSubmit(String currentUser, List<String> taskIds) {
        this.batchHandle(currentUser, taskIds, false, null);
    }

    /**
     * 批量提交或驳回工单
     */
    @Transactional
    public void batchHandle(String currentUser, List<String> taskIds, boolean reject, String reason) {
        long batchNum = IdWorker.getId();
        for (String taskId : taskIds) {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            this.handleByBatch(currentUser, task, reject, reason, batchNum);
        }
    }

    /**
     * 按批次提交或驳回工单
     */
    @Transactional
    public void handleByBatch(String currentUser, Task task, boolean reject, String reason, long batchNum) {
        Date now = new Date();

        ProcessInfo processInfo = this.validateProcessStatus(task.getProcessInstanceId());
        this.validateAssigneeAndClaim(currentUser, task);

        REJECT.set(reject);

        //工单流转
        taskService.complete(task.getId());

        if (reject) {
            operationRecordService.reject(processInfo.getId(), task, reason, batchNum);
        } else {
            operationRecordService.handle(processInfo.getId(), task, reason, batchNum);
        }

        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInfo.getProcessInstanceId()).taskCreatedAfter(now).list();
        for (Task nextTask : tasks) {
            taskService.setPriority(nextTask.getId(), processInfo.getPriority());

            if (reject) {
                ProcessTag rejectTag = new ProcessTag(ProcessTag.TASK_TAG_REJECT_NAME, ProcessTagType.WARNING, reason);

                Object taskTagsObj = taskService.getVariableLocal(nextTask.getId(), ProcessTag.TASK_TAGS);
                if (taskTagsObj == null) {
                    taskTagsObj = new ArrayList<ProcessTag>();
                }
                @SuppressWarnings("unchecked") List<ProcessTag> taskTags = (List<ProcessTag>) taskTagsObj;
                taskTags.add(rejectTag);

                taskService.setVariableLocal(nextTask.getId(), ProcessTag.TASK_TAGS, taskTags);
            }
        }
    }

    /**
     * 实时提交表单数据（不支持扩展块的实时提交）
     */
    @Transactional
    public void submitFormdata(String currentUser, String taskId, String key, String value) {
        ProcessInfo processInfo = this.validate(currentUser, taskId);

        UserTask userTask = getTaskDefByTaskInst(processInfo.getTask());

        FormTemplateField field = formTemplateFieldService.getByFormIdAndFieldKey(processInfo.getFormTemplateId(), key);
        Assert.validateId(field, "表单字段", key);

        //保存表单数据
        formDataService.saveFormData(processInfo, userTask, field, value);
    }

    /**
     * 附件上传
     */
    @Transactional
    public List<FormDataAttach> upload(String currentUser, String taskId, String key, Integer batchNum, List<MultipartFile> files) {
        ProcessInfo processInfo = this.validate(currentUser, taskId);

        FormTemplateField field = formTemplateFieldService.getByFormIdAndFieldKey(processInfo.getFormTemplateId(), key);
        Assert.validateId(field, "表单字段", key);

        List<Document> documentList = documentService.upload(files);

        return formDataService.upload(processInfo, field, batchNum, documentList);
    }

    /**
     * 删除附件
     */
    @Transactional
    public void deleteAttach(String currentUser, String taskId, Long formDataAttachId) {
        ProcessInfo processInfo = this.validate(currentUser, taskId);
        FormDataAttach formDataAttach = formDataAttachService.getById(formDataAttachId);
        Assert.validateId(formDataAttach, "表单数据附件", formDataAttachId);

        if (!processInfo.getId().equals(formDataAttach.getProcessInfoId())) {
            throw new CheckedException("任务附件关系错误");
        }
        formDataAttachService.delete(formDataAttachId);
    }

    /**
     * 任务改派
     */
    @Transactional
    public void reassign(String taskId, String currentUser, String reassignee, String remark) {
        ProcessInfo processInfo = this.validate(currentUser, taskId);
        Task task = processInfo.getTask();

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
    public void suspend(String currentUser, Long processInfoId, String reason) {
        ProcessInfo processInfo = this.getById(processInfoId);
        Assert.validateId(processInfo, "工单信息", processInfoId);
        if (processInfo.getSuspend()) {
            throw new CheckedException("该工单已经挂起");
        }

        runtimeService.suspendProcessInstanceById(processInfo.getProcessInstanceId());

        processInfo.setSuspend(true);
        this.updateById(processInfo);

        operationRecordService.suspend(processInfoId, currentUser);

        ProcessTag guaqiTag = new ProcessTag(ProcessTag.PROCESS_TAG_GUAQI_NAME, ProcessTagType.INFO, reason);

        Object processTagsObj = runtimeService.getVariable(processInfo.getProcessInstanceId(), ProcessTag.PROCESS_TAGS);
        if (processTagsObj != null) {
            processTagsObj = new ArrayList<ProcessTag>();
        }
        @SuppressWarnings("unchecked") List<ProcessTag> processTags = (List<ProcessTag>) processTagsObj;
        processTags.add(guaqiTag);

        runtimeService.setVariable(processInfo.getProcessInstanceId(), ProcessTag.PROCESS_TAGS, processTags);

        runtimeService.suspendProcessInstanceById(processInfo.getProcessInstanceId());
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

        @SuppressWarnings("unchecked") List<ProcessTag> processTags = (List<ProcessTag>)
                runtimeService.getVariable(processInfo.getProcessInstanceId(), ProcessTag.PROCESS_TAGS);
        for (ProcessTag processTag : processTags) {
            if (ProcessTag.PROCESS_TAG_GUAQI_NAME.equals(processTag.getLabel())) {
                processTags.remove(processTag);
                runtimeService.setVariable(processInfo.getProcessInstanceId(), ProcessTag.PROCESS_TAGS, processTags);
                break;
            }
        }
    }

    /**
     * 查询待办列表
     */
    @Transactional
    public IPage<ProcessInfo> queryTasktodoList(String currentUser, IPage<ProcessInfo> page, List<String> processDefinitionName,
                                                List<String> taskName, String fuzzyTitle, String fuzzyNum, Integer priority,
                                                String processVarQueryParamsStr, String taskVarQueryParamsStr, Set<String> withVars) {

        Map<String, String> processVarQueryParams = new HashMap<>();
        if (StringUtils.isNotBlank(processVarQueryParamsStr)) {
            String[] queryParamsArray = processVarQueryParamsStr.split(",");
            for (String queryParam : queryParamsArray) {
                String[] keyValue = queryParam.split("\\^");
                processVarQueryParams.put(keyValue[0], keyValue[1]);
            }
        }

        Map<String, String> taskVarQueryParams = new HashMap<>();
        if (StringUtils.isNotBlank(taskVarQueryParamsStr)) {
            String[] queryParamsArray = taskVarQueryParamsStr.split(",");
            for (String queryParam : queryParamsArray) {
                String[] keyValue = queryParam.split("\\^");
                taskVarQueryParams.put(keyValue[0], keyValue[1]);
            }
        }

        return this.queryTasktodoList(currentUser, page, processDefinitionName, taskName, fuzzyTitle, fuzzyNum, priority, processVarQueryParams, taskVarQueryParams, withVars);
    }

    /**
     * 查询待办列表
     */
    @Transactional
    public IPage<ProcessInfo> queryTasktodoList(String currentUser, IPage<ProcessInfo> page, List<String> processDefinitionName,
                                                List<String> taskName, String fuzzyTitle, String fuzzyNum, Integer priority,
                                                Map<String, String> processVarQueryParamsStr, Map<String, String> taskVarQueryParams, Set<String> withVars) {

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
        if (StringUtils.isNotBlank(fuzzyTitle)) {
            taskQuery.processVariableValueLikeIgnoreCase(PROCESS_TITLE, '%' + fuzzyTitle + '%');
        }
        if (StringUtils.isNotBlank(fuzzyNum)) {
            taskQuery.processVariableValueLikeIgnoreCase(PROCESS_NUM, '%' + fuzzyNum + '%');
        }
        if (priority != null) {
            taskQuery.processVariableValueEquals(PROCESS_PRIORITY, priority.toString());
        }

        if (processVarQueryParamsStr != null) {
            for (Map.Entry<String, String> queryParam : processVarQueryParamsStr.entrySet()) {
                if (StringUtils.isNotBlank(queryParam.getValue())) {
                    taskQuery.processVariableValueLike(queryParam.getKey(), '%' + queryParam.getValue() + '%');
                }
            }
        }

        if (taskVarQueryParams != null) {
            for (Map.Entry<String, String> queryParam : taskVarQueryParams.entrySet()) {
                if (StringUtils.isNotBlank(queryParam.getValue())) {
                    taskQuery.taskVariableValueLike(queryParam.getKey(), '%' + queryParam.getValue() + '%');
                }
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
                //noinspection unchecked
                taskDTO.setTags((List<ProcessTag>) processTags);
            }

            if (StringUtils.isNotBlank(task.getAssignee())) {
                taskDTO.setAssignee(processUserService.getByAccount(task.getAssignee()));
            }

            if (withVars != null) {
                for (String withVar : withVars) {
                    Object variable = runtimeService.getVariable(processInfo.getProcessInstanceId(), withVar);
                    processInfo.getVariables().put(withVar, variable);
                }
            }

            int count = processRemarkService.count(JaguarLambdaQueryWrapper.<ProcessRemark>newInstance()
                    .eq(ProcessRemark::getProcessInfoId, processInfo.getId()));
            processInfo.setRemarkCount(count);
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
    public Page<ProcessInfo> queryInstanceList(String currentUser, Page<ProcessInfo> page, List<String> processDefinitionName,
                                               TaskStatus taskStatus, String fuzzyTitle, String fuzzyNum, Integer priority,
                                               String processVarQueryParamsStr, Set<String> withVars) {
        Map<String, String> processVarQueryParams = new HashMap<>();
        if (StringUtils.isNotBlank(processVarQueryParamsStr)) {
            String[] queryParamsArray = processVarQueryParamsStr.split(",");
            for (String queryParam : queryParamsArray) {
                String[] keyValue = queryParam.split("\\^");
                processVarQueryParams.put(keyValue[0], keyValue[1]);
            }
        }

        return this.queryInstanceList(currentUser, page, processDefinitionName, taskStatus, fuzzyTitle, fuzzyNum, priority, processVarQueryParams, withVars);
    }

    /**
     * 查询已办或我发起的列表
     */
    @Transactional
    public Page<ProcessInfo> queryInstanceList(String currentUser, Page<ProcessInfo> page, List<String> processDefinitionName,
                                               TaskStatus taskStatus, String fuzzyTitle, String fuzzyNum, Integer priority,
                                               Map<String, String> processVarQueryParams, Set<String> withVars) {

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
        if (StringUtils.isNotBlank(fuzzyTitle)) {
            instanceQuery.variableValueLikeIgnoreCase(PROCESS_TITLE, '%' + fuzzyTitle + '%');
        }
        if (StringUtils.isNotBlank(fuzzyNum)) {
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

                Object processTags = runtimeService.getVariable(processInstance.getId(), ProcessTag.PROCESS_TAGS);
                if (processTags != null) {
                    //noinspection unchecked
                    processInfo.setTags((List<ProcessTag>) processTags);
                }
            } else {
                processInfo.setEndTime(DateUtil.date2LocalDateTime(processInstance.getEndTime()));

                HistoricVariableInstance variableInstance = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId())
                        .variableName(ProcessTag.PROCESS_TAGS).singleResult();
                if (variableInstance != null) {
                    Object processTags = variableInstance.getValue();
                    if (processTags != null) {
                        //noinspection unchecked
                        processInfo.setTags((List<ProcessTag>) processTags);
                    }
                }
            }

            if (withVars != null) {
                for (String withVar : withVars) {
                    if (processInstance.getEndTime() == null) {
                        Object variable = runtimeService.getVariable(processInfo.getProcessInstanceId(), withVar);
                        processInfo.getVariables().put(withVar, variable);
                    } else {
                        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
                                .processInstanceId(processInfo.getProcessInstanceId()).variableName(withVar).singleResult();
                        processInfo.getVariables().put(withVar, historicVariableInstance.getValue());
                    }
                }
            }

            int count = processRemarkService.count(JaguarLambdaQueryWrapper.<ProcessRemark>newInstance()
                    .eq(ProcessRemark::getProcessInfoId, processInfo.getId()));
            processInfo.setRemarkCount(count);
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
    public Page<ProcessInfo> queryProcessInfo(Page<ProcessInfo> page, List<String> processDefinitionName,
                                              String fuzzyTitle, String fuzzyNum, Integer priority,
                                              String processVarQueryParamsStr, Set<String> withVars) {

        Map<String, String> processVarQueryParams = new HashMap<>();
        if (StringUtils.isNotBlank(processVarQueryParamsStr)) {
            String[] queryParamsArray = processVarQueryParamsStr.split(",");
            for (String queryParam : queryParamsArray) {
                String[] keyValue = queryParam.split("\\^");
                processVarQueryParams.put(keyValue[0], keyValue[1]);
            }
        }

        return this.queryProcessInfo(page, processDefinitionName, fuzzyTitle, fuzzyNum, priority, processVarQueryParams, withVars);
    }

    /**
     * 查询所有的工单
     */
    @Transactional
    public Page<ProcessInfo> queryProcessInfo(Page<ProcessInfo> page, List<String> processDefinitionName,
                                              String fuzzyTitle, String fuzzyNum, Integer priority,
                                              Map<String, String> processVarQueryParams, Set<String> withVars) {

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

        if (processVarQueryParams != null) {
            for (Map.Entry<String, String> queryParam : processVarQueryParams.entrySet()) {
                if (StringUtils.isNotBlank(queryParam.getValue())) {
                    instanceQuery.variableValueLike(queryParam.getKey(), '%' + queryParam.getValue() + '%');
                }
            }
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

                Object processTags = runtimeService.getVariable(processInstance.getId(), ProcessTag.PROCESS_TAGS);
                if (processTags != null) {
                    //noinspection unchecked
                    processInfo.setTags((List<ProcessTag>) processTags);
                }
            } else {
                processInfo.setEndTime(DateUtil.date2LocalDateTime(processInstance.getEndTime()));

                HistoricVariableInstance variableInstance = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId())
                        .variableName(ProcessTag.PROCESS_TAGS).singleResult();
                if (variableInstance != null) {
                    Object processTags = variableInstance.getValue();
                    if (processTags != null) {
                        //noinspection unchecked
                        processInfo.setTags((List<ProcessTag>) processTags);
                    }
                }
            }

            if (withVars != null) {
                for (String withVar : withVars) {
                    if (processInstance.getEndTime() == null) {
                        Object variable = runtimeService.getVariable(processInfo.getProcessInstanceId(), withVar);
                        processInfo.getVariables().put(withVar, variable);
                    } else {
                        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
                                .processInstanceId(processInfo.getProcessInstanceId()).variableName(withVar).singleResult();
                        processInfo.getVariables().put(withVar, historicVariableInstance.getValue());
                    }
                }
            }

            int count = processRemarkService.count(JaguarLambdaQueryWrapper.<ProcessRemark>newInstance()
                    .eq(ProcessRemark::getProcessInfoId, processInfo.getId()));
            processInfo.setRemarkCount(count);
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
        Task task = this.getTaskById(taskId);

        ProcessInfo processInfo = this.validateProcessStatus(task.getProcessInstanceId());
        this.validateAssignee(currentUser, task);

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        processInfo.setProcessDefinitionName(bpmnModel.getMainProcess().getName());

        //获取当前任务的表单权限
        org.flowable.bpmn.model.UserTask userTask = (org.flowable.bpmn.model.UserTask) bpmnModel.getFlowElement(task.getTaskDefinitionKey());
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
        Map<String, String> handlerInfo;
        try {
            handlerInfo = BeanUtils.describe(processUser);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new CheckedException(e);
        }

        String orgName = processOrgService.getOrgNameByUserAccount(currentUser);
        handlerInfo.put("orgName", orgName);

        Map<String, Object> variables = runtimeService.getVariables(processInfo.getProcessInstanceId());
        variables.put(PROCESS_HANDLER, handlerInfo);
        variables.put(PROCESS_NOW, new Date());

        FormTemplate formTemplate = formTemplateService.getById(processInfo.getFormTemplateId());
        processInfo.setFormTemplate(formTemplate);

        //获取当前任务的的表单块
        if (StringUtils.isNotBlank(task.getFormKey())) {
            String[] formKeys = task.getFormKey().split(",");
            for (String formKey : formKeys) {
                FormTemplateSheet taskSheet = formTemplateSheetService.getByFormIdAndElementId(formTemplate.getId(), formKey);

                formTemplateSheetService.fillSheetComponent(taskSheet);

                formTemplateFieldService.resolveExpressionValue(taskSheet, variables);

                taskSheet.setBatchNum(0);

                //填充表单数据
                if (taskSheet.getOverride()) {
                    //通常情况下，如果任务曾被处理过（驳回的情况下）
                    JaguarLambdaQueryWrapper<FormData> wrapper = JaguarLambdaQueryWrapper.newInstance();
                    wrapper.eq(FormData::getProcessInfoId, processInfo.getId());
                    wrapper.eq(FormData::getFormTemplateSheetId, taskSheet.getId());
                    if (formDataService.exists(wrapper)) {
                        formDataService.fillFormData(processInfo.getId(), taskSheet);
                    }
                } else {//扩展块的情况下
                    List<FormTemplateSheet> sheets = formDataService.fillFormDataForExtendSheet(processInfo.getId(), taskSheet);
                    formTemplate.getFormTemplateSheets().addAll(sheets);
                    taskSheet.setBatchNum(sheets.size());
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

        //获取已经生成的任务
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
            if (StringUtils.isNotBlank(taskInstance.getAssignee())) {
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

    /**
     * 获取审核结果
     */
    public Boolean getAuditResult() {
        return !ProcessInfoService.REJECT.get();
    }
}
