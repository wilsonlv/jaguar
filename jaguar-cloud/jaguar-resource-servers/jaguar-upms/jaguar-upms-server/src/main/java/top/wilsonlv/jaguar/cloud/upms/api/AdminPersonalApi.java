package top.wilsonlv.jaguar.cloud.upms.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import top.wilsonlv.jaguar.cloud.upms.entity.ResourceServer;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.MenuVO;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.RandomPassword;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.ResourceServerVO;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
import top.wilsonlv.jaguar.cloud.upms.service.PersonalService;
import top.wilsonlv.jaguar.cloud.upms.service.UserService;
import top.wilsonlv.jaguar.commons.data.encryption.util.EncryptionUtil;
import top.wilsonlv.jaguar.commons.oauth2.util.SecurityUtil;
import top.wilsonlv.jaguar.commons.web.JsonResult;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author lvws
 * @since 2021/11/1
 */
@RestController
@RequestMapping("/admin/personal")
@RequiredArgsConstructor
@Api(tags = "个人授权查询")
public class AdminPersonalApi {

    private final UserService userService;

    private final PersonalService personalService;

    @ApiOperation(value = "获取个人信息")
    @GetMapping("/getUserInfo")
    public JsonResult<UserVO> getUserInfo() {
        return JsonResult.success(userService.getDetail(SecurityUtil.getCurrentUserId()));
    }

    @ApiOperation(value = "修改个人信息")
    @PostMapping("/modifyUserInfo")
    public JsonResult<UserVO> modifyUserInfo(@RequestParam(required = false) String userPhone,
                                             @RequestParam(required = false) String userEmail,
                                             @RequestParam(required = false) String userNickName) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        personalService.modifyUserInfo(currentUserId, userPhone, userEmail, userNickName);
        return getUserInfo();
    }

    @ApiOperation(value = "修改个人密码")
    @PostMapping("/modifyPassword")
    public JsonResult<Void> modifyPassword(@RequestParam @NotBlank String oldPassword,
                                           @RequestParam @NotBlank String newPassword) {
        personalService.modifyPassword(SecurityUtil.getCurrentUserId(), oldPassword, newPassword);
        return JsonResult.success();
    }

    @ApiOperation(value = "获取个人菜单")
    @GetMapping("/getUserMenus")
    public JsonResult<List<MenuVO>> getUserMenus(@RequestParam @NotBlank String serverId) {
        return JsonResult.success(personalService.getUserMenus(serverId, SecurityUtil.getCurrentUserId()));
    }

    @ApiOperation(value = "获取个人资源服务")
    @GetMapping("/getUserResourceServers")
    public JsonResult<List<ResourceServerVO>> getUserResourceServers() {
        return JsonResult.success(personalService.getUserResourceServers(SecurityUtil.getCurrentUserId()));
    }

}
