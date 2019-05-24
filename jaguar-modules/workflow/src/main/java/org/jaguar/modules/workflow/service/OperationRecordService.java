package org.jaguar.modules.workflow.service;

import org.jaguar.core.base.BaseService;
import org.jaguar.modules.workflow.enums.ProcessOperationType;
import org.jaguar.modules.workflow.mapper.OperationRecordMapper;
import org.jaguar.modules.workflow.model.po.OperationRecord;
import org.jaguar.modules.workflow.model.po.ProcessInfo;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

    /**
     * 操作记录
     */
    @Transactional
    public void initiate(ProcessInfo processInfo, String initiator, String processDefinitionName) {
        OperationRecord record = new OperationRecord();
        record.setProcessInfoId(processInfo.getId());
        record.setOperator(initiator);
        record.setProcessOperationType(ProcessOperationType.INITIATE);
        record.setOperateTime(new Date());
        record.setTaskName(processDefinitionName);
        this.insert(record);
    }

    /**
     * 操作记录
     */
    @Transactional
    public void operate(ProcessInfo processInfo, Task task, ProcessOperationType processOperationType) {
        OperationRecord record = new OperationRecord();
        record.setProcessInfoId(processInfo.getId());
        record.setOperator(task.getAssignee());
        record.setProcessOperationType(processOperationType);
        record.setOperateTime(new Date());
        record.setTaskName(task.getName());
        record.setTaskInstId(task.getId());
        record.setTaskDefId(task.getTaskDefinitionKey());
        this.insert(record);
    }

    /**
     * 改派
     */
    public void reassign(ProcessInfo processInfo, Task task, String assignee) {
        OperationRecord record = new OperationRecord();
        record.setProcessInfoId(processInfo.getId());
        record.setOperator(task.getAssignee());
        record.setProcessOperationType(ProcessOperationType.REASSIGN);
        record.setOperateTime(new Date());
        record.setTaskName(task.getName());
        record.setTaskInstId(task.getId());
        record.setTaskDefId(task.getTaskDefinitionKey());
        record.setAssignee(assignee);
        this.insert(record);
    }

    /**
     * 取消
     */
    public void cancel(ProcessInfo processInfo) {
        OperationRecord record = new OperationRecord();
        record.setProcessInfoId(processInfo.getId());
        record.setOperator(processInfo.getInitiator());
        record.setProcessOperationType(ProcessOperationType.CANCEL);
        record.setOperateTime(new Date());
        this.insert(record);
    }
}
