package org.jaguar.modules.system.mgm.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.modules.system.mgm.mapper.UserRoleMapper;
import org.jaguar.modules.system.mgm.model.UserRole;
import org.jaguar.modules.system.mgm.service.UserRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 系统用户角色表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-11-15
 */
@Validated
@RestController
@RequestMapping("/system/mgm/user_role")
@Api(value = "系统用户角色表管理")
public class UserRoleController extends AbstractController<UserRole, UserRoleMapper, UserRoleService> {

    @ApiOperation(value = "修改系统用户角色表")
    @RequiresPermissions("系统用户角色表:新增编辑")
    @PostMapping
    public ResponseEntity<JsonResult<UserRole>> create(@RequestBody UserRole userRole) {

        userRole = service.create(userRole);
        return success(userRole);
    }

    @ApiOperation(value = "删除系统用户角色表")
    @RequiresPermissions("系统用户角色表:删除")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<JsonResult<?>> del(@PathVariable Long id) {
        return super.delete(id);
    }

}