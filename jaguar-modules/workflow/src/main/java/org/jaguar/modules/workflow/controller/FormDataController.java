package org.jaguar.modules.workflow.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.Page;
import org.jaguar.modules.workflow.mapper.FormDataMapper;
import org.jaguar.modules.workflow.model.po.FormData;
import org.jaguar.modules.workflow.service.FormDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lvws
 * @since 2019/11/5
 */
@Validated
@RestController
@RequestMapping("/process/form_data")
@Api(value = "表单数据表管理")
public class FormDataController extends AbstractController<FormData, FormDataMapper, FormDataService> {

    @ApiOperation(value = "查询表单数据表")
    @RequiresPermissions("process_info_mgm")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<FormData>>> page(
            @ApiParam(value = "分页信息") com.baomidou.mybatisplus.extension.plugins.pagination.Page<FormData> page,
            @ApiParam(value = "工单ID", required = true) @RequestParam @NotNull Long processInfoId,
            @ApiParam(value = "模糊字段标签") @RequestParam(required = false) String fuzzyFieldLabel,
            @ApiParam(value = "模糊字段Key") @RequestParam(required = false) String fuzzyFieldKey) {

        page = service.queryOverride(page, processInfoId, fuzzyFieldLabel, fuzzyFieldKey);
        return success(page);
    }

    @ApiOperation(value = "编辑表单数据表")
    @RequiresPermissions("process_info_mgm")
    @PostMapping(value = "/{id}")
    public ResponseEntity<JsonResult<FormData>> update(
            @ApiParam(value = "表单数据ID") @PathVariable Long id,
            @ApiParam(value = "表单数据值") @RequestBody @NotBlank String value) {

        FormData formData = service.modifyValue(id, value);
        return success(formData);
    }
}
