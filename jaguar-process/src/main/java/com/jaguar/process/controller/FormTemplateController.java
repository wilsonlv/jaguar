package com.jaguar.process.controller;

import com.jaguar.web.base.AbstractController;
import com.jaguar.core.exception.BusinessException;
import com.jaguar.core.util.Assert;
import com.jaguar.core.util.InstanceUtil;
import com.jaguar.process.model.po.FormTemplate;
import com.jaguar.process.service.FormTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import com.jaguar.web.JsonResult;
import static com.jaguar.core.constant.Constant.*;

/**
 * <p>
 * 表单模版表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
@RestController
@Api(value = "表单模版表管理", description = "表单模版表管理")
@RequestMapping("/form_template")
public class FormTemplateController extends AbstractController<FormTemplateService> {

    @ApiOperation(value = "查询表单模版表")
    @RequiresPermissions("formTemplate:read")
    @GetMapping(value = "/list")
    public ResponseEntity<JsonResult> query(
                        @ApiParam(value = "页码") @RequestParam(required = false) Integer page,
                        @ApiParam(value = "页量") @RequestParam(required = false) Integer rows,
                        @ApiParam(value = "表单名称") @RequestParam(required = false) String name,
                        @ApiParam(value = "表单元素ID") @RequestParam(required = false) String elementId,
                        @ApiParam(value = "只查询最新版") @RequestParam(required = false, defaultValue = "true") boolean latest) {

        Map<String, Object> param = InstanceUtil.newHashMap();
        param.put(PAGE, page);
        param.put(ROWS, rows);
        param.put(DELETED, 0);

        param.put("name", name);
        param.put("elementId", elementId);

        if (latest) {
            return setSuccessJsonResult( service.queryLatest(param));
        }
        return super.query( param);
    }

    @ApiOperation(value = "表单模版表详情")
    @RequiresPermissions("formTemplate:read")
    @GetMapping(value = "/detail/{id}")
    public ResponseEntity<JsonResult> get( @PathVariable Long id) {

        FormTemplate formTemplate = service.getFormComponentById(id);
        return setSuccessJsonResult( formTemplate);
    }

    @ApiOperation(value = "表单模版表详情")
    @RequiresPermissions("formTemplate:read")
    @GetMapping(value = "/detail_by_elementid/{elementId}")
    public ResponseEntity<JsonResult> getByElementId( @PathVariable String elementId) {

        FormTemplate formTemplate = service.getFormComponentByElementId(elementId);
        return setSuccessJsonResult( formTemplate);
    }

    @ApiOperation(value = "发布表单模版")
    @RequiresPermissions("formTemplate:update")
    @PostMapping(value = "/deploy")
    public ResponseEntity<JsonResult> deploy( @RequestBody FormTemplate formTemplate) {
        Assert.notNull(formTemplate.getElementId(), "表单模版元素ID");
        Assert.notNull(formTemplate.getName(), "表单名称");
        if (formTemplate.getFormTemplateSheets() == null || formTemplate.getFormTemplateSheets().size() == 0) {
            throw new BusinessException("至少需要配置一个表单块！");
        }

        synchronized (this) {
            formTemplate = service.deployForm(formTemplate);
        }
        return setSuccessJsonResult( formTemplate);
    }

}