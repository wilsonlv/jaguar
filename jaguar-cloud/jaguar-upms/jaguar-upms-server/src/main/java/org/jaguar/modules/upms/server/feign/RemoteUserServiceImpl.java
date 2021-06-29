package org.jaguar.modules.upms.server.feign;

import lombok.RequiredArgsConstructor;
import org.jaguar.modules.upms.sdk.feign.RemoteUserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public UserDetails loadUserByUsername(String username) {
        return User.withUsername(username)
                .password(passwordEncoder.encode("123456"))
                .authorities("ROLE_ADMIN").build();
    }

}
