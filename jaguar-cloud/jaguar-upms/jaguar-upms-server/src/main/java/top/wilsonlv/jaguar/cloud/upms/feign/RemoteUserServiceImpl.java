package top.wilsonlv.jaguar.cloud.upms.feign;

import lombok.RequiredArgsConstructor;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
import top.wilsonlv.jaguar.cloud.upms.service.UserService;
import top.wilsonlv.jaguar.cloud.upms.sdk.feign.RemoteUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.wilsonlv.jaguar.commons.web.JsonResult;

import javax.validation.constraints.NotBlank;

/**
 * @author lvws
 * @since 2021/6/29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/feign/user")
public class RemoteUserServiceImpl implements RemoteUserService {

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;


    @Override
    @GetMapping("/loadUserByUsername")
    public JsonResult<UserVO> loadUserByUsername(@RequestParam @NotBlank String username) {
        return JsonResult.success(userService.getByPrincipalWithRoleAndPermission(username));
    }

}
