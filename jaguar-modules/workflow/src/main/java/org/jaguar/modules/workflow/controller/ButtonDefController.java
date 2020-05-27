package org.jaguar.modules.workflow.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.Page;
import org.jaguar.modules.workflow.mapper.ButtonDefMapper;
import org.jaguar.modules.workflow.model.po.ButtonDef;
import org.jaguar.modules.workflow.service.ButtonDefService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


/**
 * <p>
 * 按钮定义表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-04-04
 */
@Validated
@RestController
@RequestMapping("/process/button_def")
@Api(value = "按钮定义表管理")
public class ButtonDefController extends AbstractController<ButtonDef, ButtonDefMapper, ButtonDefService> {

    @Override
    @ApiOperation(value = "查询按钮定义表")
    @RequiresPermissions("process_button_def_view")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<ButtonDef>>> query(
            @ApiIgnore com.baomidou.mybatisplus.extension.plugins.pagination.Page<ButtonDef> page) {

        return super.query(page);
    }

    @Override
    @ApiOperation(value = "按钮定义表详情")
    @RequiresPermissions("process_button_def_view")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<ButtonDef>> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @ApiOperation(value = "修改按钮定义表")
    @RequiresPermissions("process_button_def_update")
    @PostMapping
    public ResponseEntity<JsonResult<ButtonDef>> update(ButtonDef buttonDef) {
        return super.saveOrUpdate(buttonDef);
    }

    @Override
    @ApiOperation(value = "删除按钮定义表")
    @RequiresPermissions("process_button_def_del")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<JsonResult<?>> delete(@PathVariable Long id) {
        return super.delete(id);
    }

}