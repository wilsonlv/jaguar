package org.jaguar.modules.system.mgm.controller;

import io.swagger.annotations.Api;
import org.jaguar.commons.basecrud.BaseController;
import org.jaguar.modules.system.mgm.mapper.RoleMenuMapper;
import org.jaguar.modules.system.mgm.model.RoleMenu;
import org.jaguar.modules.system.mgm.service.RoleMenuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Api(tags = "系统角色菜单表管理")
public class RoleMenuController extends BaseController<RoleMenu, RoleMenuMapper, RoleMenuService> {

    /*@ApiOperation(value = "查询系统角色菜单表")
    @RequiresPermissions("系统角色菜单表:读取")
    @GetMapping(value = "/tree_menu_with_role_permission/{roleId}")
    public JsonResult<List<Menu>> treeMenuWithRolePermission(@PathVariable Long roleId) {

        List<Menu> menuList = service.treeMenuWithRolePermission(roleId);
        return success(menuList);
    }

    @ApiOperation(value = "修改系统角色菜单表")
    @RequiresPermissions("系统角色菜单表:新增编辑")
    @PostMapping
    public JsonResult<RoleMenu> change(@RequestBody @Valid RoleMenu roleMenu) {
        synchronized (this) {
            roleMenu = service.change(roleMenu);
        }
        return success(roleMenu);
    }*/

}