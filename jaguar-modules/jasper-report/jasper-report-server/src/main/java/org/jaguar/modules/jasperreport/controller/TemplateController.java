package org.jaguar.modules.jasperreport.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.Page;
import org.jaguar.modules.jasperreport.mapper.TemplateMapper;
import org.jaguar.modules.jasperreport.model.Template;
import org.jaguar.modules.jasperreport.service.TemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * jasperReport模板表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2020-06-03
 */
@Validated
@RestController
@RequestMapping("/jasper_report/template")
@Api(tags = "jasperReport模板表管理")
public class TemplateController extends AbstractController<Template, TemplateMapper, TemplateService> {


    @ApiOperation(value = "查询jasperReport模板表")
    @RequiresPermissions("jasperReport模板表:读取")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<Template>>> page(
            @ApiIgnore com.baomidou.mybatisplus.extension.plugins.pagination.Page<Template> page,
            @ApiParam(value = "模糊模板名称") @RequestParam(required = false) String fuzzyTemplateName,
            @ApiParam(value = "模糊模板类别") @RequestParam(required = false) String fuzzyTemplateType) {

        LambdaQueryWrapper<Template> wrapper = JaguarLambdaQueryWrapper.<Template>newInstance()
                .like(Template::getTemplateName, fuzzyTemplateName)
                .like(Template::getTemplateType, fuzzyTemplateType);
        page = service.queryWithDocument(page, wrapper);
        return success(page);
    }

    @ApiOperation(value = "jasperReport模板表详情")
    @RequiresPermissions("jasperReport模板表:读取")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<Template>> detail(@PathVariable Long id) {
        Template template = service.getDetail(id);
        return success(template);
    }

    @ApiOperation(value = "修改jasperReport模板表")
    @RequiresPermissions("jasperReport模板表:新增编辑")
    @PostMapping
    public ResponseEntity<JsonResult<Template>> update(@RequestBody @Valid Template template) {
        template = service.createOrUpdate(template);
        return success(template);
    }

    @ApiOperation(value = "上传jasperReport模板")
    @RequiresPermissions("jasperReport模板表:新增编辑")
    @PostMapping(value = "/upload/{id}")
    public ResponseEntity<JsonResult<Template>> upload(@PathVariable Long id,
                                                       @ApiParam(value = "jasper模板", required = true) @RequestParam @NotNull MultipartFile file) {

        Template template = service.upload(id, file);
        return success(template);
    }

    @ApiOperation(value = "删除jasperReport模板表")
    @RequiresPermissions("jasperReport模板表:删除")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<JsonResult<?>> del(@PathVariable Long id) {
        return super.delete(id);
    }

}