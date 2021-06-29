package org.jaguar.modules.upms.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jaguar.commons.basecrud.BaseController;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.commons.web.JsonResult;
import org.jaguar.modules.upms.sdk.dto.MenuFunction;
import org.jaguar.modules.upms.server.mapper.RoleMapper;
import org.jaguar.modules.upms.server.model.Role;
import org.jaguar.modules.upms.server.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

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
@RequestMapping("/system/mgm/role")
@Api(tags = "系统角色表管理")
public class RoleController extends BaseController<Role, RoleMapper, RoleService> {

    @ApiOperation(value = "查询系统角色表")
    @PreAuthorize("hasAuthority('角色管理')")
    @GetMapping(value = "/page")
    public JsonResult<Page<Role>> page(
            @ApiIgnore Page<Role> page,
            @ApiParam(value = "模糊角色名称") @RequestParam(required = false) String fuzzyRoleName,
            @ApiParam(value = "角色是否启用") @RequestParam(required = false) Boolean roleEnable) {

        LambdaQueryWrapper<Role> wrapper = JaguarLambdaQueryWrapper.<Role>newInstance().
                like(Role::getRoleName, fuzzyRoleName)
                .eq(Role::getRoleEnable, roleEnable);

        page = service.queryWithUser(page, wrapper);
        return success(page);
    }

    @ApiOperation(value = "系统角色表详情")
    @PreAuthorize("hasAuthority('角色管理')")
    @GetMapping(value = "/{id}")
    public JsonResult<Role> detail(@PathVariable Long id) {

        Role role = service.getDetail(id);
        return success(role);
    }

    @ApiOperation(value = "修改系统角色表")
    @PreAuthorize("hasAuthority('角色管理')")
    @PostMapping
    public JsonResult<Role> update(@Valid @RequestBody Role role) {
        synchronized (this) {
            service.createOrUpdate(role);
        }
        return success(role);
    }

    @ApiOperation(value = "删除系统角色表")
    @PreAuthorize("hasAuthority('角色管理')")
    @DeleteMapping(value = "/{id}")
    public JsonResult<?> del(@PathVariable Long id) {
        synchronized (this) {
            service.checkAndDelete(id);
        }
        return success();
    }

    @ApiOperation(value = "获取所有菜单和功能")
    @PreAuthorize("hasAuthority('角色管理')")
    @GetMapping(value = "/menu_functions")
    public JsonResult<List<MenuFunction>> menuFunctions() {
        return success(MenuFunction.MENU_FUNCTIONS);
    }
}