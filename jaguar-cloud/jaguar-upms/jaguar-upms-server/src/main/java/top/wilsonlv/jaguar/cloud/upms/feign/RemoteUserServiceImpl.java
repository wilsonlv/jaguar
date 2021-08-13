package top.wilsonlv.jaguar.cloud.upms.feign;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import top.wilsonlv.jaguar.cloud.upms.model.User;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
import top.wilsonlv.jaguar.cloud.upms.service.UserService;
import top.wilsonlv.jaguar.commons.enums.UserType;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityAuthority;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityUser;
import top.wilsonlv.jaguar.cloud.upms.sdk.feign.RemoteUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.wilsonlv.jaguar.commons.oauth2.util.SecurityUtil;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

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
    public UserVO loadUserByUsername(@RequestParam @NotBlank String username) {
        return userService.getByPrincipalWithRoleAndPermission(username);
    }

}
