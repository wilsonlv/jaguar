package top.wilsonlv.jaguar.cloud.upms.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
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

    private final TokenStore tokenStore;

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

    @ApiOperation(value = "登出")
    @DeleteMapping("/logout")
    public JsonResult<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(token.substring(7));
        // 清空access token
        tokenStore.removeAccessToken(accessToken);

        // 清空 refresh token
        OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
        if (refreshToken != null) {
            tokenStore.removeRefreshToken(refreshToken);
        }
        return JsonResult.success();
    }

}
