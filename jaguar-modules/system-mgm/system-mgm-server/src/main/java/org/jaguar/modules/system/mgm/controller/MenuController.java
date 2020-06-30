package org.jaguar.modules.system.mgm.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.modules.system.mgm.mapper.MenuMapper;
import org.jaguar.modules.system.mgm.model.Menu;
import org.jaguar.modules.system.mgm.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 系统菜单表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Validated
@RestController
@RequestMapping("/system/mgm/menu")
@Api(value = "系统菜单表管理")
public class MenuController extends AbstractController<Menu, MenuMapper, MenuService> {

    @ApiOperation(value = "查询系统菜单表")
    @RequiresPermissions("系统菜单表:读取")
    @GetMapping(value = "/tree")
    public ResponseEntity<JsonResult<List<Menu>>> tree() {
        List<Menu> menuTree = service.tree();
        return success(menuTree);
    }

    @ApiOperation(value = "系统菜单表详情")
    @RequiresPermissions("系统菜单表:读取")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<Menu>> detail(@PathVariable Long id) {
        return super.getById(id);
    }

    @ApiOperation(value = "修改系统菜单表")
    @RequiresPermissions("系统菜单表:新增编辑")
    @PostMapping
    public ResponseEntity<JsonResult<Menu>> update(@RequestBody @Valid Menu menu) {

        menu = service.createOrUpdate(menu);
        return success(menu);
    }

    @ApiOperation(value = "删除系统菜单表")
    @RequiresPermissions("系统菜单表:删除")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<JsonResult<?>> del(@PathVariable Long id) {

        service.deleteWithRoleMenuId(id);
        return success();
    }

}