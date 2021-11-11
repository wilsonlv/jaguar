package top.wilsonlv.jaguar.cloud.upms.feign;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.wilsonlv.jaguar.cloud.upms.sdk.feign.RemoteUserService;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.MenuVO;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
import top.wilsonlv.jaguar.cloud.upms.service.AuthService;
import top.wilsonlv.jaguar.cloud.upms.service.UserService;
import top.wilsonlv.jaguar.commons.web.JsonResult;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author lvws
 * @since 2021/6/29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/feign/user")
public class RemoteUserServiceImpl implements RemoteUserService {

    private final UserService userService;

    private final AuthService authService;

    @Override
    @GetMapping("/loadUserByUsername")
    public JsonResult<UserVO> loadUserByUsername(@RequestParam @NotBlank String username) {
        return JsonResult.success(authService.getByPrincipalWithRoleAndPermission(username));
    }

    @Override
    @GetMapping("/getDetail")
    public JsonResult<UserVO> getDetail(@RequestParam Long userId) {
        return JsonResult.success(userService.getDetail(userId));
    }

    @Override
    @GetMapping("/getUserMenus")
    public JsonResult<List<MenuVO>> getUserMenus(@RequestParam Long userId) {
        return JsonResult.success(authService.getUserMenus(userId));
    }

    @Override
    @PostMapping("/modifyUserInfo")
    public JsonResult<Void> modifyUserInfo(@RequestParam Long userId,
                                           @RequestParam(required = false) String userPhone,
                                           @RequestParam(required = false) String userEmail,
                                           @RequestParam(required = false) String userNickName) {
        authService.modifyUserInfo(userId, userPhone, userEmail, userNickName);
        return JsonResult.success();
    }

    @Override
    @PostMapping("/modifyPassword")
    public JsonResult<Void> modifyPassword(@RequestParam Long userId,
                                           @RequestParam @NotBlank String oldPassword,
                                           @RequestParam @NotBlank String newPassword) {
        authService.modifyPassword(userId, oldPassword, newPassword);
        return JsonResult.success();
    }

}
