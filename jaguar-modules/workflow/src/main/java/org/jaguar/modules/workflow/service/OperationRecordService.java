package org.jaguar.modules.workflow.service;

import org.jaguar.core.base.BaseService;

import org.apache.commons.lang3.StringUtils;
import org.flowable.task.api.Task;
import org.jaguar.modules.workflow.enums.ProcessOperationType;
import org.jaguar.modules.workflow.mapper.OperationRecordMapper;
import org.jaguar.modules.workflow.model.po.OperationRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 工单操作记录表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-04-10
 */
@Service
public class OperationRecordService extends BaseService<OperationRecord, OperationRecordMapper> {

    @Autowired
    private IProcessUserService processUserService;

    @Transactional
    public List<OperationRecord> listByProcessInfoIdWithUser(Long processInfoId) {
        List<OperationRecord> operationRecords = this.mapper.listGroupByBatchNum(processInfoId);

        for (OperationRecord operationRecord : operationRecords) {
            operationRecord.setOperatorUser(processUserService.getByAccount(operationRecord.getOperator()));
            if (StringUtils.isNotBlank(operationRecord.getAssignee())) {
                operationRecord.setAssigneeUser(processUserService.getByAccount(operationRecord.getAssignee()));
            }
        }
        return operationRecords;
    }


    /**
     * 发起
     */
    @Transactional
    public void initiate(Long processInfoId, String initiator, String processDefinitionName) {
        OperationRecord record = new OperationRecord();
        record.setProcessInfoId(processInfoId);
        record.setOperator(initiator);
        record.setProcessOperationType(ProcessOperationType.INITIATE);
        record.setOperateTime(LocalDateTime.now());
        record.setTaskName(processDefinitionName);
        this.insert(record);
    }

    /**
     * 处理
     */
    @Transactional
    public void handle(Long processInfoId, Task task) {
        this.handle(processInfoId, task, null);
    }

    /**
     * 处理
     */
    @Transactional
    public void handle(Long processInfoId, Task task, String batchNum) {
        OperationRecord record = new OperationRecord();
        record.setProcessInfoId(processInfoId);
        record.setOperator(task.getAssignee());
        record.setProcessOperationType(ProcessOperationType.HANDLE);
        record.setOperateTime(LocalDateTime.now());
        record.setTaskName(task.getName());
        record.setTaskInstId(task.getId());
        record.setTaskDefId(task.getTaskDefinitionKey());
        record.setBatchNum(batchNum);
        this.insert(record);
    }

    /**
     * 改派
     */
    @Transactional
    public void reassign(Long processInfoId, Task task, String assignee, String reassignee, String remark) {
        OperationRecord record = new OperationRecord();
        record.setProcessInfoId(processInfoId);
        record.setOperator(assignee);
        record.setProcessOperationType(ProcessOperationType.REASSIGN);
        record.setOperateTime(LocalDateTime.now());
        record.setTaskName(task.getName());
        record.setTaskInstId(task.getId());
        record.setTaskDefId(task.getTaskDefinitionKey());
        record.setAssignee(reassignee);
        record.setReason(remark);
        this.insert(record);
    }

    /**
     * 取消
     */
    @Transactional
    public void cancel(Long processInfoId, String operator, @NotBlank String reason) {
        OperationRecord record = new OperationRecord();
        record.setProcessInfoId(processInfoId);
        record.setOperator(operator);
        record.setProcessOperationType(ProcessOperationType.CANCEL);
        record.setOperateTime(LocalDateTime.now());
        record.setReason(reason);
        this.insert(record);
    }

    /**
     * 驳回
     */
    @Transactional
    public void reject(Long processInfoId, Task task, @NotBlank String reason) {
        this.reject(processInfoId, task, reason, null);
    }

    /**
     * 驳回
     */
    @Transactional
    public void reject(Long processInfoId, Task task, @NotBlank String reason, String batchNum) {
        OperationRecord record = new OperationRecord();
        record.setProcessInfoId(processInfoId);
        record.setOperator(task.getAssignee());
        record.setProcessOperationType(ProcessOperationType.REJECT);
        record.setOperateTime(LocalDateTime.now());
        record.setTaskName(task.getName());
        record.setTaskInstId(task.getId());
        record.setTaskDefId(task.getTaskDefinitionKey());
        record.setReason(reason);
        record.setBatchNum(batchNum);
        this.insert(record);
    }

    /**
     * 回退
     */
    @Deprecated
    @Transactional
    public void goback(Long processInfoId, Task task, @NotBlank String reason, String batchNum) {
        OperationRecord record = new OperationRecord();
        record.setProcessInfoId(processInfoId);
        record.setOperator(task.getAssignee());
        record.setProcessOperationType(ProcessOperationType.GOBACK);
        record.setOperateTime(LocalDateTime.now());
        record.setTaskName(task.getName());
        record.setTaskInstId(task.getId());
        record.setTaskDefId(task.getTaskDefinitionKey());
        record.setReason(reason);
        record.setBatchNum(batchNum);
        this.insert(record);
    }

    /**
     * 挂起
     */
    @Transactional
    public void suspend(Long processInfoId, String currentUser) {
        OperationRecord record = new OperationRecord();
        record.setProcessInfoId(processInfoId);
        record.setOperator(currentUser);
        record.setProcessOperationType(ProcessOperationType.SUSPEND);
        record.setOperateTime(LocalDateTime.now());
        this.insert(record);
    }

    /**
     * 激活
     */
    @Transactional
    public void activate(Long processInfoId, String currentUser) {
        OperationRecord record = new OperationRecord();
        record.setProcessInfoId(processInfoId);
        record.setOperator(currentUser);
        record.setProcessOperationType(ProcessOperationType.ACTIVATE);
        record.setOperateTime(LocalDateTime.now());
        this.insert(record);
    }
}
