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
@RequestMapping("/system/user_role")
@Api(value = "系统用户角色表管理")
public class UserRoleController extends AbstractController<UserRole, UserRoleMapper, UserRoleService> {


    @ApiOperation(value = "查询系统用户角色表")
    @RequiresPermissions("系统用户角色表:读取")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<UserRole>>> page(
            @ApiParam(value = "分页信息") com.baomidou.mybatisplus.extension.plugins.pagination.Page<UserRole> page,
            @ApiParam(value = "查询信息") UserRole userRole) {

        LambdaQueryWrapper<UserRole> wrapper = new JaguarLambdaQueryWrapper<>();
        wrapper.setEntity(userRole);
        return super.query(page, wrapper);
    }

    @ApiOperation(value = "系统用户角色表详情")
    @RequiresPermissions("系统用户角色表:读取")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<UserRole>> detail(@PathVariable Long id) {
        return super.getById(id);
    }

    @ApiOperation(value = "修改系统用户角色表")
    @RequiresPermissions("系统用户角色表:新增编辑")
    @PostMapping(value = "/update")
    public ResponseEntity<JsonResult<UserRole>> update(@RequestBody @NotNull UserRole userRole) {
        return super.saveOrUpdate(userRole);
    }

    @ApiOperation(value = "删除系统用户角色表")
    @RequiresPermissions("系统用户角色表:删除")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<JsonResult<?>> del(@PathVariable Long id) {
        return super.delete(id);
    }

}