package org.jaguar.modules.upms.sdk.component;

import lombok.RequiredArgsConstructor;
import org.jaguar.modules.upms.sdk.feign.RemoteUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author lvws
 * @since 2021/6/30
 */
@Component
@RequiredArgsConstructor
public class JaguarUserDetailsServiceImpl implements UserDetailsService {

    private final RemoteUserService remoteUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return remoteUserService.loadUserByUsername(username);
    }
}
