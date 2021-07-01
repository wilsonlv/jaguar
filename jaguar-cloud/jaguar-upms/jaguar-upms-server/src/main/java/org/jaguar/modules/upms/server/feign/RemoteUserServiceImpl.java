package org.jaguar.modules.upms.server.feign;

import lombok.RequiredArgsConstructor;
import org.jaguar.commons.oauth2.model.SecurityAuthority;
import org.jaguar.commons.oauth2.model.SecurityUser;
import org.jaguar.modules.upms.sdk.feign.RemoteUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
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

    @Override
    @GetMapping("/loadUserByUsername")
    public SecurityUser loadUserByUsername(@RequestParam @NotBlank String username) {
        SecurityUser user = new SecurityUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setAuthorities(new ArrayList<SecurityAuthority>() {{
            add(new SecurityAuthority("ROLE_ADMIN"));
        }});
        return user;
    }

}
