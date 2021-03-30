package org.jaguar.modules.system.mgm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.Page;
import org.jaguar.modules.system.mgm.enums.DataScope;
import org.jaguar.modules.system.mgm.mapper.UserMapper;
import org.jaguar.modules.system.mgm.model.User;
import org.jaguar.modules.system.mgm.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

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
@RequestMapping("/system/mgm/user")
@Api(tags = "系统用户表管理")
public class UserController extends AbstractController<User, UserMapper, UserService> {

    @ApiOperation(value = "查询系统用户表")
    @RequiresPermissions("系统用户表:读取")
    @GetMapping(value = "/page")
    public JsonResult<Page<User>> page(
            @ApiIgnore com.baomidou.mybatisplus.extension.plugins.pagination.Page<User> page,
            @ApiParam(value = "模糊用户信息") @RequestParam(required = false) String fuzzyUserInfo,
            @ApiParam(value = "角色数据权限（PERSONAL：个人的，SUBLEVEL：个人及子级别，CURRENT_LEVEL：个人及本级，UNLIMITED：无限制）")
            @RequestParam(required = false) DataScope userDataScope,
            @ApiParam(value = "锁定状态") @RequestParam(required = false) Boolean userLocked) {

        LambdaQueryWrapper<User> wrapper = JaguarLambdaQueryWrapper.<User>newInstance()
                .eq(User::getUserLocked, userLocked)
                .eq(User::getUserDataScope, userDataScope);
        if (StringUtils.isNotBlank(fuzzyUserInfo)) {
            wrapper.and(w -> w.like(User::getUserAccount, fuzzyUserInfo).or()
                    .like(User::getUserPhone, fuzzyUserInfo).or()
                    .like(User::getUserEmail, fuzzyUserInfo));
        }

        page = service.queryWithRoleAndDept(page, wrapper);
        return success(page);
    }

    @ApiOperation(value = "系统用户表详情")
    @RequiresPermissions("系统用户表:读取")
    @GetMapping(value = "/{id}")
    public JsonResult<User> detail(@PathVariable Long id) {
        User user = service.getDetail(id);
        return success(user);
    }

    @ApiOperation(value = "修改系统用户表")
    @RequiresPermissions("系统用户表:新增编辑")
    @PostMapping
    public JsonResult<User> update(@RequestBody @Valid User user) {
        synchronized (this) {
            if (user.getId() == null) {
                user = service.create(user);
            } else {
                user = service.modify(user);
            }
        }
        return success(user);
    }

    @ApiOperation(value = "重置密码")
    @RequiresPermissions("系统用户表:新增编辑")
    @PostMapping(value = "/reset_password/{id}")
    public JsonResult<String> resetPassword(@PathVariable Long id) {
        String resetPassword = service.resetPassword(id);
        return success(resetPassword);
    }

    @ApiOperation(value = "锁定解锁用户")
    @RequiresPermissions("系统用户表:新增编辑")
    @PostMapping(value = "/toggle_lock/{id}")
    public JsonResult<Boolean> toggleLock(@PathVariable Long id) {
        Boolean userLocked = service.toggleLock(id);
        return success(userLocked);
    }

}