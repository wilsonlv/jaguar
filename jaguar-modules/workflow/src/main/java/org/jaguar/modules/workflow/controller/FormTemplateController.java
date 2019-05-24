package org.jaguar.modules.workflow.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.exception.CheckedException;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.Page;
import org.jaguar.modules.workflow.mapper.FormTemplateMapper;
import org.jaguar.modules.workflow.model.po.FormTemplate;
import org.jaguar.modules.workflow.service.FormTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * <p>
 * 表单模版表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
@Validated
@RestController
@RequestMapping("/process/form_template")
@Api(value = "表单模版表管理", description = "表单模版表管理")
public class FormTemplateController extends AbstractController<FormTemplate, FormTemplateMapper, FormTemplateService> {

    @ApiOperation(value = "查询表单模版表")
    @RequiresPermissions("process_form_template_view")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<FormTemplate>>> page(
            @ApiParam(value = "分页信息") com.baomidou.mybatisplus.extension.plugins.pagination.Page<FormTemplate> page,
            @ApiParam(value = "表单名称") String name,
            @ApiParam(value = "模糊表单名称") String fuzzyName,
            @ApiParam(value = "表单元素ID") String elementId,
            @ApiParam(value = "只查询最新版") @RequestParam(required = false, defaultValue = "true") Boolean latest) {

        if (latest) {
            return success(service.queryLatest(page, name, fuzzyName, elementId));
        }

        JaguarLambdaQueryWrapper<FormTemplate> wrapper = new JaguarLambdaQueryWrapper<>();
        wrapper.eq(FormTemplate::getName, name)
                .like(FormTemplate::getName, fuzzyName)
                .eq(FormTemplate::getElementId, elementId)
                .select(FormTemplate::getId, FormTemplate::getName, FormTemplate::getElementId, FormTemplate::getVersion);
        return super.page(page, wrapper);
    }

    @ApiOperation(value = "表单模版表详情")
    @RequiresPermissions("process_form_template_view")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<FormTemplate>> get(@PathVariable Long id) {

        FormTemplate formTemplate = service.getFormComponentById(id);
        return success(formTemplate);
    }

    @ApiOperation(value = "表单模版表详情")
    @RequiresPermissions("process_form_template_view")
    @GetMapping(value = "/detail_by_elementid/{elementId}")
    public ResponseEntity<JsonResult<FormTemplate>> getByElementId(@PathVariable String elementId) {

        FormTemplate formTemplate = service.getFormComponentByElementId(elementId);
        return success(formTemplate);
    }

    @ApiOperation(value = "发布表单模版")
    @RequiresPermissions("process_form_template_update")
    @PostMapping(value = "/deploy")
    public ResponseEntity<JsonResult<FormTemplate>> deploy(@Valid @RequestBody FormTemplate formTemplate) {
        if (formTemplate.getFormTemplateSheets() == null || formTemplate.getFormTemplateSheets().size() == 0) {
            throw new CheckedException("至少需要配置一个表单块！");
        }

        synchronized (this) {
            formTemplate = service.deployForm(formTemplate);
        }
        return success(formTemplate);
    }

}