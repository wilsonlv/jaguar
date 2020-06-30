package org.jaguar.modules.workflow.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.Page;
import org.jaguar.modules.workflow.mapper.ProcessInfoMapper;
import org.jaguar.modules.workflow.model.po.ProcessInfo;
import org.jaguar.modules.workflow.model.vo.FlowDefinition;
import org.jaguar.modules.workflow.service.FlowDefinitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author lvws
 * @since 2019/3/15.
 */
@Validated
@RestController
@RequestMapping("/process/flow_definition")
@Api(value = "流程定义表管理")
public class FlowDefinitionController extends AbstractController<ProcessInfo, ProcessInfoMapper, FlowDefinitionService> {

    @ApiOperation(value = "查询流程定义表")
    @RequiresPermissions("process_flow_definition_view")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<FlowDefinition>>> page(
            @ApiIgnore com.baomidou.mybatisplus.extension.plugins.pagination.Page<FlowDefinition> page,
            @ApiParam(value = "流程名称") @RequestParam(required = false) String name,
            @ApiParam(value = "模糊流程名称") @RequestParam(required = false) String fuzzyName,
            @ApiParam(value = "只查询最新版") @RequestParam(required = false, defaultValue = "true") Boolean latest) {

        IPage<FlowDefinition> flowDefinitions = service.queryFlow(page, name, fuzzyName, latest);
        return success(flowDefinitions);
    }

    @ApiOperation(value = "流程定义表详情")
    @RequiresPermissions("process_flow_definition_view")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<FlowDefinition>> get(@PathVariable String id) {

        FlowDefinition flowDefinition = service.getFlow(id);
        return success(flowDefinition);
    }

    @ApiOperation(value = "发布流程定义")
    @RequiresPermissions("process_flow_definition_update")
    @PostMapping(value = "/deploy")
    public ResponseEntity<JsonResult<String>> deploy(@RequestBody FlowDefinition flowDefinition) {

        String processDefinitionId;
        synchronized (this) {
            processDefinitionId = service.deployFlow(flowDefinition);
        }
        return success(processDefinitionId);
    }

}
