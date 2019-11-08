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
import org.jaguar.modules.system.mgm.mapper.UserMapper;
import org.jaguar.modules.system.mgm.model.User;
import org.jaguar.modules.system.mgm.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 系统用户表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Validated
@RestController
@RequestMapping("/system/user")
@Api(value = "系统用户表管理", description = "系统用户表管理")
public class UserController extends AbstractController<User, UserMapper, UserService> {


    @ApiOperation(value = "查询系统用户表")
    @RequiresPermissions("user:读取")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<User>>> page(
            @ApiParam(value = "分页信息") com.baomidou.mybatisplus.extension.plugins.pagination.Page<User> page,
            @ApiParam(value = "查询信息") User user) {

        LambdaQueryWrapper<User> wrapper = new JaguarLambdaQueryWrapper<>();
        wrapper.setEntity(user);
        return super.page(page, wrapper);
    }

    @ApiOperation(value = "系统用户表详情")
    @RequiresPermissions("user:读取")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<User>> detail(@PathVariable Long id) {
        return super.getById(id);
    }

    @ApiOperation(value = "修改系统用户表")
    @RequiresPermissions("user:update")
    @PostMapping(value = "/update")
    public ResponseEntity<JsonResult<User>> update(@RequestBody @NotNull User user) {
        return super.saveOrUpdate(user);
    }

    @ApiOperation(value = "删除系统用户表")
    @RequiresPermissions("user:del")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity del(@PathVariable Long id) {
        return super.delete(id);
    }

}