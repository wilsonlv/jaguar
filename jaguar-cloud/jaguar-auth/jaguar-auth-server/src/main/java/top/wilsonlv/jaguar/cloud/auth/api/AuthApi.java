package top.wilsonlv.jaguar.cloud.auth.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import top.wilsonlv.jaguar.cloud.upms.sdk.feign.RemoteUserService;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.MenuVO;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.RandomPassword;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
import top.wilsonlv.jaguar.commons.data.encryption.util.EncryptionUtil;
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

    private final RemoteUserService userService;

    private final TokenStore tokenStore;

    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "获取个人信息")
    @GetMapping("/getUserInfo")
    public JsonResult<UserVO> getUserInfo() {
        return userService.getDetail(SecurityUtil.getCurrentUserId());
    }

    @ApiOperation(value = "获取个人菜单")
    @GetMapping("/getUserMenus")
    public JsonResult<List<MenuVO>> getUserMenus() {
        return userService.getUserMenus(SecurityUtil.getCurrentUserId());
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

    @ApiOperation(value = "获取随机密码")
    @GetMapping("/randomPassword")
    public JsonResult<RandomPassword> randomPassword(@RequestParam(required = false, defaultValue = "2") Integer upperCaseLetterLength,
                                                     @RequestParam(required = false, defaultValue = "2") Integer lowerCaseLetterLength,
                                                     @RequestParam(required = false, defaultValue = "4") Integer numLength) {
        String randomPassword = EncryptionUtil.randomPassword(upperCaseLetterLength, lowerCaseLetterLength, numLength);
        String encode = passwordEncoder.encode(randomPassword);
        return JsonResult.success(new RandomPassword(randomPassword, encode));
    }


}
