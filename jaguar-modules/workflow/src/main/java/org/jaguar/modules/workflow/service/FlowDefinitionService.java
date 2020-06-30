package org.jaguar.modules.workflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.jaguar.core.base.BaseService;
import org.jaguar.modules.workflow.Constant;
import org.jaguar.modules.workflow.mapper.ProcessInfoMapper;
import org.jaguar.modules.workflow.model.po.ProcessInfo;
import org.jaguar.modules.workflow.model.vo.FlowDefinition;
import org.jaguar.modules.workflow.util.Bpmn20Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lvws
 * @since 2019/3/14.
 */
@Slf4j
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
        log.info("开始部署【{}】", flowDefinition.getName());
        Deployment deploy = repositoryService.createDeployment()
                .addBpmnModel(flowDefinition.getName() + Constant.RESOURCE_NAME_PATTERN, bpmnModel).deploy();
        long duration = (System.currentTimeMillis() - startTime) / 1000;
        log.info("【{}】部署完成，耗时：{}s", flowDefinition.getName(), duration);

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploy.getId()).singleResult();
        return processDefinition.getId();
    }

    /**
     * 根据流程名称获取流程定义基本信息列表
     *
     * @param latest 是否只查询最新版
     */
    public IPage<FlowDefinition> queryFlow(IPage<FlowDefinition> page, String name, String fuzzyName, boolean latest) {

        int first = (int) ((page.getCurrent() - 1) * page.getSize());
        int offset = (int) page.getSize();

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        if (StringUtils.isNotBlank(name)) {
            processDefinitionQuery = processDefinitionQuery.processDefinitionName(name);
        }
        if (StringUtils.isNotBlank(fuzzyName)) {
            processDefinitionQuery = processDefinitionQuery.processDefinitionNameLike(fuzzyName);
        }

        if (latest) {
            processDefinitionQuery = processDefinitionQuery.latestVersion();
        }

        List<ProcessDefinition> processDefinitions = processDefinitionQuery.listPage(first, offset);
        List<FlowDefinition> flowDefinitions = new ArrayList<>();
        for (ProcessDefinition processDefinition : processDefinitions) {
            FlowDefinition flowDefinition = new FlowDefinition();
            flowDefinition.setId(processDefinition.getId());
            flowDefinition.setName(processDefinition.getName());
            flowDefinition.setDescription(processDefinition.getDescription());
            flowDefinition.setVersion(processDefinition.getVersion());
            flowDefinitions.add(flowDefinition);
        }

        page.setTotal(processDefinitionQuery.count());
        page.setRecords(flowDefinitions);
        return page;
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
