package top.wilsonlv.jaguar.cloud.upms.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wilsonlv.jaguar.cloud.upms.controller.vo.MenuVO;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
import top.wilsonlv.jaguar.cloud.upms.service.AuthService;
import top.wilsonlv.jaguar.cloud.upms.service.UserService;
import top.wilsonlv.jaguar.commons.oauth2.util.SecurityUtil;
import top.wilsonlv.jaguar.commons.web.JsonResult;

import java.util.List;

/**
 * @author lvws
 * @since 2021/11/1
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(tags = "个人授权查询")
public class AuthApi {

    private final UserService userService;

    private final AuthService authService;

    @ApiOperation(value = "获取个人信息")
    @GetMapping("/getUserInfo")
    public JsonResult<UserVO> getUserInfo() {
        return JsonResult.success(userService.getDetail(SecurityUtil.getCurrentUserId()));
    }

    @ApiOperation(value = "获取个人菜单")
    @GetMapping("/getUserMenus")
    public JsonResult<List<MenuVO>> getUserMenus() {
        return JsonResult.success(authService.getUserMenus(SecurityUtil.getCurrentUserId()));
    }

}
