package org.jaguar.modules.upms.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.jaguar.commons.basecrud.BaseController;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.commons.web.JsonResult;
import org.jaguar.modules.upms.sdk.enums.DataScope;
import org.jaguar.modules.upms.server.mapper.UserMapper;
import org.jaguar.modules.upms.server.model.User;
import org.jaguar.modules.upms.server.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class UserController extends BaseController<User, UserMapper, UserService> {

    @ApiOperation(value = "查询系统用户表")
    @PreAuthorize("hasAuthority('用户管理')")
    @GetMapping(value = "/page")
    public JsonResult<Page<User>> page(
            @ApiIgnore Page<User> page,
            @ApiParam(value = "模糊用户信息") @RequestParam(required = false) String fuzzyUserInfo,
            @ApiParam(value = "角色数据权限") @RequestParam(required = false) DataScope userDataScope,
            @ApiParam(value = "锁定状态") @RequestParam(required = false) Boolean userLocked,
            @ApiParam(value = "启用状态") @RequestParam(required = false) Boolean userEnable) {

        LambdaQueryWrapper<User> wrapper = JaguarLambdaQueryWrapper.<User>newInstance()
                .eq(User::getUserLocked, userLocked)
                .eq(User::getUserEnable, userEnable)
                .eq(User::getUserDataScope, userDataScope);
        if (StringUtils.isNotBlank(fuzzyUserInfo)) {
            wrapper.and(w -> w.like(User::getUserAccount, fuzzyUserInfo).or()
                    .like(User::getUserPhone, fuzzyUserInfo).or()
                    .like(User::getUserEmail, fuzzyUserInfo));
        }

        page = service.queryWithRole(page, wrapper);
        return success(page);
    }

    @ApiOperation(value = "系统用户表详情")
    @PreAuthorize("hasAuthority('用户管理')")
    @GetMapping(value = "/{id}")
    public JsonResult<User> detail(@PathVariable Long id) {
        User user = service.getDetail(id);
        return success(user);
    }

    @ApiOperation(value = "修改系统用户表")
    @PreAuthorize("hasAuthority('用户管理')")
    @PostMapping
    public JsonResult<Void> update(@RequestBody @Valid User user) {
        synchronized (this) {
            if (user.getId() == null) {
                service.create(user);
            } else {
                service.modify(user);
            }
        }
        return success();
    }

}