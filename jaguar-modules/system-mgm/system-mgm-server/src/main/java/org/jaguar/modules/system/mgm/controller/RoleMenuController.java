package org.jaguar.modules.system.mgm.controller;

import org.jaguar.modules.system.mgm.model.RoleMenu;
import org.jaguar.modules.system.mgm.mapper.RoleMenuMapper;
import org.jaguar.modules.system.mgm.service.RoleMenuService;

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
 * 系统角色菜单表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Validated
@RestController
@RequestMapping("/system/role_menu")
@Api(value = "系统角色菜单表管理")
public class RoleMenuController extends AbstractController<RoleMenu, RoleMenuMapper, RoleMenuService> {


    @ApiOperation(value = "查询系统角色菜单表")
    @RequiresPermissions("系统角色菜单表:读取")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<RoleMenu>>> page(
        @ApiParam(value = "分页信息") com.baomidou.mybatisplus.extension.plugins.pagination.Page<RoleMenu> page,
        @ApiParam(value = "查询信息") RoleMenu roleMenu) {

        LambdaQueryWrapper<RoleMenu> wrapper = new JaguarLambdaQueryWrapper<>();
        wrapper.setEntity(roleMenu);
        return super.query(page, wrapper);
    }

    @ApiOperation(value = "系统角色菜单表详情")
    @RequiresPermissions("系统角色菜单表:读取")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<RoleMenu>> detail(@PathVariable Long id){
        return super.getById(id);
    }

    @ApiOperation(value = "修改系统角色菜单表")
    @RequiresPermissions("系统角色菜单表:新增编辑")
    @PostMapping
    public ResponseEntity<JsonResult<RoleMenu>> update(@RequestBody @NotNull RoleMenu roleMenu){
        return super.saveOrUpdate(roleMenu);
    }

    @ApiOperation(value = "删除系统角色菜单表")
    @RequiresPermissions("系统角色菜单表:删除")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<JsonResult<?>> del(@PathVariable Long id){
        return super.delete(id);
    }

}