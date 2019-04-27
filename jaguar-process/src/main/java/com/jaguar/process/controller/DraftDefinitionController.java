package com.jaguar.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jaguar.core.enums.OrderType;
import com.jaguar.core.util.Assert;
import com.jaguar.core.util.InstanceUtil;
import com.jaguar.process.enums.DefinitionType;
import com.jaguar.process.model.vo.FlowDefinition;
import com.jaguar.web.JsonResult;
import com.jaguar.web.base.AbstractController;

import com.jaguar.process.model.po.DraftDefinition;
import com.jaguar.process.model.po.FormTemplate;
import com.jaguar.process.service.DraftDefinitionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.jaguar.core.constant.Constant.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-03-06
 */
@RestController
@Api(value = "草稿表管理", description = "草稿表管理")
@RequestMapping("/draft_definition")
public class DraftDefinitionController extends AbstractController<DraftDefinitionService> {

    @ApiOperation(value = "查询草稿表")
    @RequiresPermissions("draftDefinition:read")
    @GetMapping(value = "/list")
    public ResponseEntity<JsonResult> query(
            @ApiParam(value = "页码") @RequestParam(required = false) Integer page,
            @ApiParam(value = "页量") @RequestParam(required = false) Integer rows,
            @ApiParam(value = "只查询最新版") @RequestParam(defaultValue = "true") boolean latest,
            @ApiParam(value = "草稿类型（FORM：表单，FLOW：流程）", required = true) @RequestParam DefinitionType definitionType) {

        Map<String, Object> param = InstanceUtil.newHashMap();
        param.put(PAGE, page);
        param.put(ROWS, rows);
        param.put(SORT, "create_time");
        param.put(ORDER, OrderType.DESC);
        param.put(DELETED, 0);

        param.put("definitionType", definitionType.toString());

        Page<DraftDefinition> draftDefinitionPage;
        if (latest) {
            draftDefinitionPage = service.queryLatest(param);
        } else {
            draftDefinitionPage = service.query(param);
        }

        for (DraftDefinition draftDefinition : draftDefinitionPage.getRecords()) {
            draftDefinition.setContext(null);
        }
        return setSuccessJsonResult(draftDefinitionPage);
    }

    @ApiOperation(value = "草稿表详情")
    @RequiresPermissions("draftDefinition:read")
    @GetMapping(value = "/detail/{id}")
    public ResponseEntity<JsonResult> get(@PathVariable Long id) {
        return super.get(id);
    }

    @ApiOperation(value = "表单草稿详情")
    @RequiresPermissions("draftDefinition:read")
    @GetMapping(value = "/form/{id}")
    public ResponseEntity<JsonResult> getForm(@PathVariable Long id) {

        FormTemplate formTemplate = service.getForm(id);
        return setSuccessJsonResult(formTemplate);
    }

    @ApiOperation(value = "流程草稿详情")
    @RequiresPermissions("draftDefinition:read")
    @GetMapping(value = "/flow/{id}")
    public ResponseEntity<JsonResult> getFlow(@PathVariable Long id) {

        FlowDefinition flowDefinition = service.getFlow(id);
        return setSuccessJsonResult(flowDefinition);
    }

    @ApiOperation(value = "保存表单草稿")
    @RequiresPermissions("draftDefinition:update")
    @PostMapping(value = "/save_form")
    public ResponseEntity<JsonResult> saveForm(@RequestBody FormTemplate formTemplate) {
        Assert.notNull(formTemplate.getName(), "表单名称");

        synchronized (this) {
            service.saveForm(formTemplate);
        }
        return setSuccessJsonResult();
    }

    @ApiOperation(value = "保存流程草稿")
    @RequiresPermissions("draftDefinition:update")
    @PostMapping(value = "/save_flow")
    public ResponseEntity<JsonResult> saveFlow(@RequestBody FlowDefinition flowDefinition) {

        synchronized (this) {
            service.saveFlow(flowDefinition);
        }
        return setSuccessJsonResult();
    }

    @ApiOperation(value = "草稿表删除")
    @RequiresPermissions("draftDefinition:update")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<JsonResult> delete(@PathVariable Long id) {
        return super.delete(id);
    }
}