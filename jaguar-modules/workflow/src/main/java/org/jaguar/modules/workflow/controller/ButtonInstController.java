package org.jaguar.modules.workflow.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.Page;
import org.jaguar.modules.workflow.mapper.ButtonInstMapper;
import org.jaguar.modules.workflow.model.po.ButtonInst;
import org.jaguar.modules.workflow.service.ButtonInstService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


/**
 * <p>
 * 按钮实例表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-04-04
 */
@Validated
@RestController
@RequestMapping("/process/button_inst")
@Api(value = "按钮实例表管理")
public class ButtonInstController extends AbstractController<ButtonInst, ButtonInstMapper, ButtonInstService> {

    @ApiOperation(value = "查询按钮实例表")
    @RequiresPermissions("process_button_def_view")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<ButtonInst>>> query(
            @ApiIgnore com.baomidou.mybatisplus.extension.plugins.pagination.Page<ButtonInst> page,
            @ApiParam(value = "流程名称") @RequestParam(required = false) String processDefinitionkey,
            @ApiParam(value = "任务名称") @RequestParam(required = false) String taskDefName) {

        LambdaQueryWrapper<ButtonInst> wrapper = JaguarLambdaQueryWrapper.<ButtonInst>newInstance()
                .eq(ButtonInst::getProcessDefinitionkey, processDefinitionkey)
                .eq(ButtonInst::getTaskDefName, taskDefName);

        page = service.queryWithButtonDef(page, wrapper);
        return success(page);
    }

    @Override
    @ApiOperation(value = "按钮实例表详情")
    @RequiresPermissions("process_button_def_view")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<ButtonInst>> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @ApiOperation(value = "修改按钮实例表")
    @RequiresPermissions("process_button_def_update")
    @PostMapping
    public ResponseEntity<JsonResult<ButtonInst>> update(ButtonInst param) {
        return super.saveOrUpdate(param);
    }

    @Override
    @ApiOperation(value = "删除按钮实例表")
    @RequiresPermissions("process_button_def_del")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<JsonResult<?>> delete(@PathVariable Long id) {
        return super.delete(id);
    }

}