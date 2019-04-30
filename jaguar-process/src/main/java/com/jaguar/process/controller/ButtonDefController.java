package com.jaguar.process.controller;

import com.jaguar.core.util.InstanceUtil;
import com.jaguar.process.model.po.ButtonDef;
import com.jaguar.process.service.ButtonDefService;
import com.jaguar.web.JsonResult;
import com.jaguar.web.base.AbstractController;
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
 * 按钮定义表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-04-04
 */
@RestController
@Api(value = "按钮定义表管理", description = "按钮定义表管理")
@RequestMapping("/button_def")
public class ButtonDefController extends AbstractController<ButtonDefService> {

    @ApiOperation(value = "查询按钮定义表")
    @RequiresPermissions("buttonDef:read")
    @GetMapping(value = "/list")
    public ResponseEntity<JsonResult> query(@ApiParam(value = "页码") @RequestParam(required = false) Integer page,
                                            @ApiParam(value = "页量") @RequestParam(required = false) Integer rows) {

        Map<String, Object> param = InstanceUtil.newHashMap();
        param.put(PAGE, page);
        param.put(ROWS, rows);

        return super.query(param);
    }

    @ApiOperation(value = "按钮定义表详情")
    @RequiresPermissions("buttonDef:read")
    @GetMapping(value = "/detail/{id}")
    public ResponseEntity<JsonResult> get(@PathVariable Long id) {
        return super.get(id);
    }

    @ApiOperation(value = "修改按钮定义表")
    @RequiresPermissions("buttonDef:update")
    @PostMapping(value = "/update")
    public ResponseEntity<JsonResult> update(ButtonDef param) {
        return super.update(param);
    }

    @ApiOperation(value = "删除按钮定义表")
    @RequiresPermissions("buttonDef:update")
    @DeleteMapping(value = "/del/{id}")
    public ResponseEntity<JsonResult> del(@PathVariable Long id) {
        return super.del(id);
    }

    @ApiOperation(value = "删除按钮定义表")
    @RequiresPermissions("buttonDef:update")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<JsonResult> delete(@PathVariable Long id) {
        return super.delete(id);
    }

}