package com.jaguar.process.service;

import com.jaguar.process.mapper.ProcessOperationRecordMapper;
import com.jaguar.process.model.po.ProcessInfo;
import com.jaguar.process.model.po.ProcessOperationRecord;
import com.jaguar.core.base.BaseService;
import com.jaguar.process.enums.ProcessOperationType;
import org.flowable.task.api.Task;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 流程操作记录表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-04-10
 */
@Service
@CacheConfig(cacheNames = "ProcessOperationRecord")
public class ProcessOperationRecordService extends BaseService<ProcessOperationRecord, ProcessOperationRecordMapper> {

    /**
     * 操作记录
     */
    @Transactional
    public void initiate(ProcessInfo processInfo, String initiator, String processDefinitionName) {
        ProcessOperationRecord record = new ProcessOperationRecord();
        record.setProcessInfoId(processInfo.getId());
        record.setOperator(initiator);
        record.setProcessOperationType(ProcessOperationType.INITIATE);
        record.setOperateTime(new Date());
        record.setTaskName(processDefinitionName);
        this.update(record);
    }

    /**
     * 操作记录
     */
    @Transactional
    public void operate(ProcessInfo processInfo, Task task, ProcessOperationType processOperationType) {
        ProcessOperationRecord record = new ProcessOperationRecord();
        record.setProcessInfoId(processInfo.getId());
        record.setOperator(task.getAssignee());
        record.setProcessOperationType(processOperationType);
        record.setOperateTime(new Date());
        record.setTaskName(task.getName());
        record.setTaskInstId(task.getId());
        record.setTaskDefId(task.getTaskDefinitionKey());
        this.update(record);
    }

    /**
     * 改派
     */
    public void reassign(ProcessInfo processInfo, Task task, String assignee) {
        ProcessOperationRecord record = new ProcessOperationRecord();
        record.setProcessInfoId(processInfo.getId());
        record.setOperator(task.getAssignee());
        record.setProcessOperationType(ProcessOperationType.REASSIGN);
        record.setOperateTime(new Date());
        record.setTaskName(task.getName());
        record.setTaskInstId(task.getId());
        record.setTaskDefId(task.getTaskDefinitionKey());
        record.setAssignee(assignee);
        this.update(record);
    }

    /**
     * 取消
     */
    public void cancel(ProcessInfo processInfo) {
        ProcessOperationRecord record = new ProcessOperationRecord();
        record.setProcessInfoId(processInfo.getId());
        record.setOperator(processInfo.getInitiator());
        record.setProcessOperationType(ProcessOperationType.CANCEL);
        record.setOperateTime(new Date());
        this.update(record);
    }
}
