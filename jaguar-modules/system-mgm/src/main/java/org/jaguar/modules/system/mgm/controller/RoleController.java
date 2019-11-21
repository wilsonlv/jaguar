package org.jaguar.modules.system.mgm.controller;

import org.jaguar.modules.system.mgm.model.Role;
import org.jaguar.modules.system.mgm.mapper.RoleMapper;
import org.jaguar.modules.system.mgm.service.RoleService;

import org.jaguar.core.web.JsonResult;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.Page;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiOperation;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 系统角色表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Validated
@RestController
@RequestMapping("/system/role")
@Api(value = "系统角色表管理", description = "系统角色表管理")
public class RoleController extends AbstractController<Role, RoleMapper, RoleService> {


    @ApiOperation(value = "查询系统角色表")
    @RequiresPermissions("系统角色表:读取")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<Role>>> page(
        @ApiParam(value = "分页信息") com.baomidou.mybatisplus.extension.plugins.pagination.Page<Role> page,
        @ApiParam(value = "查询信息") Role role) {

        LambdaQueryWrapper<Role> wrapper = new JaguarLambdaQueryWrapper<>();
        wrapper.setEntity(role);
        return super.query(page, wrapper);
    }

    @ApiOperation(value = "系统角色表详情")
    @RequiresPermissions("系统角色表:读取")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<Role>> detail(@PathVariable Long id){
        return super.getById(id);
    }

    @ApiOperation(value = "修改系统角色表")
    @RequiresPermissions("系统角色表:新增编辑")
    @PostMapping(value = "/update")
    public ResponseEntity<JsonResult<Role>> update(@RequestBody @NotNull Role role){
        return super.saveOrUpdate(role);
    }

    @ApiOperation(value = "删除系统角色表")
    @RequiresPermissions("系统角色表:删除")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity del(@PathVariable Long id){
        return super.delete(id);
    }

}