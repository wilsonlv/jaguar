package org.jaguar.modules.workflow.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.bpmn.model.BpmnModel;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.Charsets;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.exception.Assert;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.Page;
import org.jaguar.modules.workflow.enums.DefinitionType;
import org.jaguar.modules.workflow.mapper.DraftDefinitionMapper;
import org.jaguar.modules.workflow.model.po.DraftDefinition;
import org.jaguar.modules.workflow.model.po.FormTemplate;
import org.jaguar.modules.workflow.model.vo.FlowDefinition;
import org.jaguar.modules.workflow.service.DraftDefinitionService;
import org.jaguar.modules.workflow.util.Bpmn20Util;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-03-06
 */
@Validated
@RestController
@RequestMapping("/process/draft_definition")
@Api(tags = "草稿表管理")
public class DraftDefinitionController extends AbstractController<DraftDefinition, DraftDefinitionMapper, DraftDefinitionService> {

    @ApiOperation(value = "查询草稿表")
    @RequiresPermissions("process_draft_definition_view")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<DraftDefinition>>> page(
            @ApiIgnore com.baomidou.mybatisplus.extension.plugins.pagination.Page<DraftDefinition> page,
            @ApiParam(value = "只查询最新版") @RequestParam(defaultValue = "true", required = false) Boolean latest,
            @ApiParam(value = "模糊草稿名称") @RequestParam(required = false) String fuzzyName,
            @ApiParam(value = "草稿类型（FORM：表单，FLOW：流程，默认全部）") @RequestParam(required = false) DefinitionType definitionType) {

        IPage<DraftDefinition> draftDefinitionPage;
        if (latest) {
            page.setDesc("a.create_time");
            draftDefinitionPage = service.queryLatest(page, new HashMap<String, Object>(2) {{
                put("fuzzyName", fuzzyName);
                if (definitionType != null) {
                    put("definitionType", definitionType.toString());
                }
            }});
        } else {
            page.setDesc("create_time");

            JaguarLambdaQueryWrapper<DraftDefinition> wrapper = JaguarLambdaQueryWrapper.newInstance();
            wrapper.like(DraftDefinition::getName, fuzzyName)
                    .eq(DraftDefinition::getDefinitionType, definitionType)
                    .select(DraftDefinition::getId, DraftDefinition::getElementId,
                            DraftDefinition::getName, DraftDefinition::getDefinitionType, DraftDefinition::getVersion);

            draftDefinitionPage = service.query(page, wrapper);
        }
        return success(draftDefinitionPage);
    }

    @Override
    @ApiOperation(value = "草稿表详情")
    @RequiresPermissions("process_draft_definition_view")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<DraftDefinition>> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @ApiOperation(value = "表单草稿详情")
    @RequiresPermissions("process_draft_definition_view")
    @GetMapping(value = "/form/{id}")
    public ResponseEntity<JsonResult<FormTemplate>> getForm(@PathVariable Long id) {

        FormTemplate formTemplate = service.getForm(id);
        return success(formTemplate);
    }

    @ApiOperation(value = "流程草稿详情")
    @RequiresPermissions("process_draft_definition_view")
    @GetMapping(value = "/flow/{id}")
    public ResponseEntity<JsonResult<FlowDefinition>> getFlow(@PathVariable Long id) {

        FlowDefinition flowDefinition = service.getFlow(id);
        return success(flowDefinition);
    }

    @ApiOperation(value = "保存表单草稿")
    @RequiresPermissions("process_draft_definition_update")
    @PostMapping(value = "/save_form")
    public ResponseEntity<JsonResult<?>> saveForm(@Valid @RequestBody FormTemplate formTemplate) {
        synchronized (this) {
            service.saveForm(formTemplate);
        }
        return success();
    }

    @ApiOperation(value = "保存流程草稿")
    @RequiresPermissions("process_draft_definition_update")
    @PostMapping(value = "/save_flow")
    public ResponseEntity<JsonResult<?>> saveFlow(@Valid @RequestBody FlowDefinition flowDefinition) {

        synchronized (this) {
            service.saveFlow(flowDefinition);
        }
        return success();
    }

    @ApiOperation(value = "导入表单草稿")
    @RequiresPermissions("process_draft_definition_import")
    @PostMapping(value = "/import_form")
    public ResponseEntity<JsonResult<?>> importForm(@ApiParam(value = "表单草稿", required = true) @NotNull MultipartFile file)
            throws IOException {
        FormTemplate formTemplate = JSONObject.parseObject(
                file.getInputStream(), StandardCharsets.UTF_8, FormTemplate.class, new Feature[]{});

        Assert.notNull(formTemplate.getName(), "表单名称");

        synchronized (this) {
            service.saveForm(formTemplate);
        }
        return success();
    }

    @ApiOperation(value = "导入流程草稿")
    @RequiresPermissions("process_draft_definition_import")
    @PostMapping(value = "/import_flow")
    public ResponseEntity<JsonResult<?>> importFlow(@ApiParam(value = "流程草稿", required = true) @NotNull MultipartFile file)
            throws IOException {

        BpmnModel bpmnModel = Bpmn20Util.xmlToBpmnModel(file.getInputStream());
        FlowDefinition flowDefinition = Bpmn20Util.parseFlow(bpmnModel);

        Assert.notNull(flowDefinition.getName(), "流程名称");

        synchronized (this) {
            service.saveFlow(flowDefinition);
        }
        return success();
    }

    @ApiOperation(value = "导出草稿")
    @RequiresPermissions("process_draft_definition_export")
    @GetMapping(value = "/export/{id}")
    public void exportForm(HttpServletResponse response, @PathVariable Long id) throws IOException {

        DraftDefinition draftDefinition = service.getById(id);

        String fileName;
        if (DefinitionType.FLOW.equals(draftDefinition.getDefinitionType())) {
            fileName = draftDefinition.getName() + ".xml";
        } else {
            fileName = draftDefinition.getName() + ".json";
        }

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, Charsets.UTF_8_NAME));

        PrintWriter writer = response.getWriter();
        writer.write(draftDefinition.getContext());
        writer.close();
    }

}