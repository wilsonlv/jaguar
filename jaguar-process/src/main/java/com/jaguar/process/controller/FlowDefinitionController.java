package com.jaguar.process.controller;

import com.jaguar.core.util.Assert;
import com.jaguar.process.model.vo.FlowDefinition;
import com.jaguar.web.JsonResult;
import com.jaguar.web.base.AbstractController;
import com.jaguar.process.service.FlowDefinitionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by lvws on 2019/3/15.
 */
@RestController
@Api(value = "流程定义表管理", description = "流程定义表管理")
@RequestMapping("/flow_definition")
public class FlowDefinitionController extends AbstractController<FlowDefinitionService> {

    @ApiOperation(value = "查询流程定义表")
    @RequiresPermissions("flowDefinition:read")
    @GetMapping(value = "/list")
    public ResponseEntity<JsonResult> query(
                        @ApiParam(value = "首条记录位置", required = true) @RequestParam Integer first,
                        @ApiParam(value = "查询数量", required = true) @RequestParam Integer offset,
                        @ApiParam(value = "流程名称") @RequestParam(required = false) String name,
                        @ApiParam(value = "只查询最新版") @RequestParam(required = false, defaultValue = "true") boolean latest) {

        List<FlowDefinition> flowDefinitions = service.queryFlow(name, latest, first, offset);
        return setSuccessJsonResult( flowDefinitions);
    }

    @ApiOperation(value = "流程定义表详情")
    @RequiresPermissions("flowDefinition:read")
    @GetMapping(value = "/detail/{id}")
    public ResponseEntity<JsonResult> get( @PathVariable String id) {

        FlowDefinition flowDefinition = service.getFlow(id);
        return setSuccessJsonResult( flowDefinition);
    }

    @ApiOperation(value = "发布流程定义")
    @RequiresPermissions("flowDefinition:update")
    @PostMapping(value = "/deploy")
    public ResponseEntity<JsonResult> deploy( @RequestBody FlowDefinition flowDefinition) {
        Assert.notNull(flowDefinition.getName(), "流程名称");
        Assert.notNull(flowDefinition.getStartState(), "开始节点");
        Assert.notNull(flowDefinition.getEndState(), "结束节点");
        Assert.notNull(flowDefinition.getFormElementId(), "表单元素ID");

        Assert.notNull(flowDefinition.getUserTaskList(), "任务节点集合");
        Assert.notNull(flowDefinition.getServiceTaskList(), "服务节点结合");
        Assert.notNull(flowDefinition.getExclusiveGatewayList(), "决策节点结合");

        Assert.notNull(flowDefinition.getNodeBoxList(), "节点坐标集合");
        Assert.notNull(flowDefinition.getLineList(), "连线坐标结合");

        String processDefinitionId;
        synchronized (this) {
            processDefinitionId = service.deployFlow(flowDefinition);
        }
        return setSuccessJsonResult( processDefinitionId);
    }

}
