package top.wilsonlv.jaguar.cloud.upms.sdk.component;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import top.wilsonlv.jaguar.cloud.upms.sdk.feign.RemoteUserService;
import top.wilsonlv.jaguar.commons.enums.UserType;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityUser;

/**
 * @author lvws
 * @since 2021/6/30
 */
@Component
@RequiredArgsConstructor
public class JaguarUserDetailsServiceImpl implements UserDetailsService {

    private final RemoteUserService remoteUserService;

    @Override
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        //TODO genju clientId shezhi usertype
        return remoteUserService.loadUserByUsername(username, UserType.USER);
    }
}
