package top.wilsonlv.jaguar.cloud.upms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.UserCreateDTO;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.UserModifyDTO;
import top.wilsonlv.jaguar.cloud.upms.entity.User;
import top.wilsonlv.jaguar.cloud.upms.mapper.UserMapper;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
import top.wilsonlv.jaguar.cloud.upms.service.UserService;
import top.wilsonlv.jaguar.commons.basecrud.BaseController;
import top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import top.wilsonlv.jaguar.commons.web.JsonResult;

import javax.validation.Valid;

/**
 * <p>
 * 用户前端控制器
 * </p>
 *
 * @author lvws
 * @since 2021-08-16
 */
@Validated
@RestController
@RequestMapping("/admin/user")
@Api(tags = "用户管理")
public class UserController extends BaseController<User, UserMapper, UserService> {

    @ApiOperation(value = "查询用户")
    @PreAuthorize("hasAuthority('用户管理')")
    @GetMapping(value = "/page")
    public JsonResult<Page<UserVO>> page(
            @ApiIgnore Page<User> page,
            @ApiParam(value = "模糊用户信息") @RequestParam(required = false) String fuzzyUserInfo,
            @ApiParam(value = "锁定状态") @RequestParam(required = false) Boolean userLocked,
            @ApiParam(value = "启用状态") @RequestParam(required = false) Boolean userEnable) {

        LambdaQueryWrapper<User> wrapper = JaguarLambdaQueryWrapper.<User>newInstance()
                .eq(User::getUserLocked, userLocked)
                .eq(User::getUserEnable, userEnable);
        if (StringUtils.isNotBlank(fuzzyUserInfo)) {
            wrapper.and(w -> w.like(User::getUserAccount, fuzzyUserInfo).or()
                    .like(User::getUserPhone, fuzzyUserInfo).or()
                    .like(User::getUserEmail, fuzzyUserInfo));
        }

        return success(service.queryWithRole(page, wrapper));
    }

    @ApiOperation(value = "用户详情")
    @PreAuthorize("hasAuthority('用户管理')")
    @GetMapping(value = "/{id}")
    public JsonResult<UserVO> detail(@PathVariable Long id) {
        UserVO user = service.getDetail(id);
        return success(user);
    }

    @ApiOperation(value = "新增用户")
    @PreAuthorize("hasAuthority('用户管理')")
    @PostMapping
    public JsonResult<Void> create(@RequestBody @Valid UserCreateDTO user) {
        service.create(user);
        return success();
    }

    @ApiOperation(value = "更新用户")
    @PreAuthorize("hasAuthority('用户管理')")
    @PutMapping
    public JsonResult<Void> modify(@RequestBody @Valid UserModifyDTO user) {
        service.modify(user);
        return success();
    }

    @ApiOperation(value = "删除用户")
    @PreAuthorize("hasAuthority('用户管理')")
    @DeleteMapping(value = "/{id}")
    public JsonResult<Void> del(@PathVariable Long id) {
        service.checkAnddelete(id);
        return success();
    }

}