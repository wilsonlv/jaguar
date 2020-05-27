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
import org.jaguar.modules.system.mgm.enums.RoleDataScope;
import org.jaguar.modules.system.mgm.mapper.RoleMapper;
import org.jaguar.modules.system.mgm.model.Role;
import org.jaguar.modules.system.mgm.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
@Api(value = "系统角色表管理")
public class RoleController extends AbstractController<Role, RoleMapper, RoleService> {


    @ApiOperation(value = "查询系统角色表")
    @RequiresPermissions("系统角色表:读取")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<Role>>> page(
            @ApiIgnore com.baomidou.mybatisplus.extension.plugins.pagination.Page<Role> page,
            @ApiParam(value = "模糊角色名称") @RequestParam(required = false) String fuzzyRoleName,
            @ApiParam(value = "角色数据权限（OWNER、DEPT、ALL）") @RequestParam(required = false) RoleDataScope roleDataScope,
            @ApiParam(value = "角色是否锁定") @RequestParam(required = false) Boolean roleLocked) {

        LambdaQueryWrapper<Role> wrapper = new JaguarLambdaQueryWrapper<>();
        wrapper.like(Role::getRoleName, fuzzyRoleName)
                .eq(Role::getRoleDataScope, roleDataScope)
                .eq(Role::getRoleLocked, roleLocked);

        page = service.queryWithUser(page, wrapper);
        return success(page);
    }

    @ApiOperation(value = "系统角色表详情")
    @RequiresPermissions("系统角色表:读取")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<Role>> detail(@PathVariable Long id) {
        return super.getById(id);
    }

    @ApiOperation(value = "修改系统角色表")
    @RequiresPermissions("系统角色表:新增编辑")
    @PostMapping
    public ResponseEntity<JsonResult<Role>> update(@RequestBody Role role) {

        role = service.createOrUpdate(role);
        return success(role);
    }

    @ApiOperation(value = "删除系统角色表")
    @RequiresPermissions("系统角色表:删除")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<JsonResult<?>> del(@PathVariable Long id) {

        service.deleteWithRoleMenu(id);
        return success();
    }

}