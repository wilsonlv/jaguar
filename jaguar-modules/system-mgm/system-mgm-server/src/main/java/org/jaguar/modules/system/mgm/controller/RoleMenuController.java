package org.jaguar.modules.system.mgm.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.modules.system.mgm.mapper.RoleMenuMapper;
import org.jaguar.modules.system.mgm.model.Menu;
import org.jaguar.modules.system.mgm.model.RoleMenu;
import org.jaguar.modules.system.mgm.service.RoleMenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

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
@RequestMapping("/system/mgm/role_menu")
@Api(value = "系统角色菜单表管理")
public class RoleMenuController extends AbstractController<RoleMenu, RoleMenuMapper, RoleMenuService> {

    @ApiOperation(value = "查询系统角色菜单表")
    @RequiresPermissions("系统角色菜单表:读取")
    @GetMapping(value = "/tree_menu_with_role_permission")
    public ResponseEntity<JsonResult<List<Menu>>> treeMenuWithRolePermission(
            @ApiParam(required = true, value = "角色ID") @RequestParam @NotNull Long roleId) {

        List<Menu> menuList = service.treeMenuWithRolePermission(roleId);
        return success(menuList);
    }

    @ApiOperation(value = "修改系统角色菜单表")
    @RequiresPermissions("系统角色菜单表:新增编辑")
    @PostMapping
    public ResponseEntity<JsonResult<RoleMenu>> change(@RequestBody @Valid RoleMenu roleMenu) {
        synchronized (this) {
            roleMenu = service.change(roleMenu);
        }
        return success(roleMenu);
    }

}