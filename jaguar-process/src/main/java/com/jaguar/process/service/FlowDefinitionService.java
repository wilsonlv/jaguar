package com.jaguar.process.service;

import com.jaguar.core.base.BaseService;
import com.jaguar.process.Constant;
import com.jaguar.process.mapper.ProcessInfoMapper;
import com.jaguar.process.model.po.ProcessInfo;
import com.jaguar.process.model.vo.FlowDefinition;
import com.jaguar.process.util.Bpmn20Util;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvws on 2019/3/12.
 */
@Service
public class FlowDefinitionService extends BaseService<ProcessInfo, ProcessInfoMapper> {

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 发布流程
     */
    @Transactional
    public String deployFlow(FlowDefinition flowDefinition) {
        BpmnModel bpmnModel = Bpmn20Util.convertBPMN(flowDefinition);

        long startTime = System.currentTimeMillis();
        logger.info("开始部署【{}】", flowDefinition.getName());
        Deployment deploy = repositoryService.createDeployment().addBpmnModel(flowDefinition.getName() + Constant.RESOURCE_NAME_PATTERN, bpmnModel).deploy();
        long duration = (System.currentTimeMillis() - startTime) / 1000;

        logger.info("【{}】部署完成，耗时：{}s", flowDefinition.getName(), duration);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();

        return processDefinition.getId();
    }

    /**
     * 根据流程名称获取流程定义基本信息列表
     *
     * @param latest 是否只查询最新版
     */
    public List<FlowDefinition> queryFlow(String name, boolean latest, int first, int offset) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        if (StringUtils.isNotBlank(name)) {
            processDefinitionQuery = processDefinitionQuery.processDefinitionName(name);
        }
        if (latest) {
            processDefinitionQuery = processDefinitionQuery.latestVersion();
        }

        List<FlowDefinition> flowDefinitions = new ArrayList<>();
        for (ProcessDefinition processDefinition : processDefinitionQuery.listPage(first, offset)) {
            FlowDefinition flowDefinition = new FlowDefinition();
            flowDefinition.setId(processDefinition.getId());
            flowDefinition.setName(processDefinition.getName());
            flowDefinition.setDescription(processDefinition.getDescription());
            flowDefinition.setVersion(processDefinition.getVersion());
            flowDefinitions.add(flowDefinition);
        }
        return flowDefinitions;
    }

    /**
     * 读取流程
     */
    public FlowDefinition getFlow(String id) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(id);

        FlowDefinition flowDefinition = Bpmn20Util.parseFlow(bpmnModel);

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
        flowDefinition.setId(processDefinition.getId());
        flowDefinition.setVersion(processDefinition.getVersion());
        return flowDefinition;
    }

}
