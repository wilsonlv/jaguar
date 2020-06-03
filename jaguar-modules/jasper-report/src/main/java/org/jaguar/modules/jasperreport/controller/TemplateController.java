package org.jaguar.modules.jasperreport.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

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
@Api(value = "jasperReport模板表管理")
public class TemplateController extends AbstractController<Template, TemplateMapper, TemplateService> {


    @ApiOperation(value = "查询jasperReport模板表")
    @RequiresPermissions("jasperReport模板表:读取")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<Template>>> page(
            @ApiIgnore com.baomidou.mybatisplus.extension.plugins.pagination.Page<Template> page) {

        LambdaQueryWrapper<Template> wrapper = new JaguarLambdaQueryWrapper<>();
        return super.query(page, wrapper);
    }

    @ApiOperation(value = "jasperReport模板表详情")
    @RequiresPermissions("jasperReport模板表:读取")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<Template>> detail(@PathVariable Long id) {
        return super.getById(id);
    }

    @ApiOperation(value = "修改jasperReport模板表")
    @RequiresPermissions("jasperReport模板表:新增编辑")
    @PostMapping
    public ResponseEntity<JsonResult<Template>> update(@RequestBody @Valid Template template) {
        return super.saveOrUpdate(template);
    }

    @ApiOperation(value = "删除jasperReport模板表")
    @RequiresPermissions("jasperReport模板表:删除")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<JsonResult<?>> del(@PathVariable Long id) {
        return super.delete(id);
    }

}