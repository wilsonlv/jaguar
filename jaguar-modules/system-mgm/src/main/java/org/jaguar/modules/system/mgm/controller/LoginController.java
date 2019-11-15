package org.jaguar.modules.system.mgm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.Page;
import org.jaguar.modules.system.mgm.mapper.LoginMapper;
import org.jaguar.modules.system.mgm.model.Login;
import org.jaguar.modules.system.mgm.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 系统登陆日志表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-11-15
 */
@Validated
@RestController
@RequestMapping("/system/login")
@Api(value = "系统登陆日志表管理", description = "系统登陆日志表管理")
public class LoginController extends AbstractController<Login, LoginMapper, LoginService> {


    @ApiOperation(value = "查询系统登陆日志表")
    @RequiresPermissions("系统登陆日志表:读取")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<Login>>> page(
            @ApiParam(value = "分页信息") com.baomidou.mybatisplus.extension.plugins.pagination.Page<Login> page,
            @ApiParam(value = "查询信息") Login login) {

        LambdaQueryWrapper<Login> wrapper = new JaguarLambdaQueryWrapper<>();
        wrapper.setEntity(login);
        return super.page(page, wrapper);
    }

    @ApiOperation(value = "系统登陆日志表详情")
    @RequiresPermissions("系统登陆日志表:读取")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<Login>> detail(@PathVariable Long id) {
        return super.getById(id);
    }

    @ApiOperation(value = "修改系统登陆日志表")
    @RequiresPermissions("系统登陆日志表:新增编辑")
    @PostMapping(value = "/update")
    public ResponseEntity<JsonResult<Login>> update(@RequestBody @NotNull Login login) {
        return super.saveOrUpdate(login);
    }

    @ApiOperation(value = "删除系统登陆日志表")
    @RequiresPermissions("系统登陆日志表:删除")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity del(@PathVariable Long id) {
        return super.delete(id);
    }

}