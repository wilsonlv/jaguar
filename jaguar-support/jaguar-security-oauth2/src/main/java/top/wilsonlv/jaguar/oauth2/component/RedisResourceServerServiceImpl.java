package top.wilsonlv.jaguar.oauth2.component;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.stereotype.Component;
import top.wilsonlv.jaguar.oauth2.Oauth2Constant;
import top.wilsonlv.jaguar.oauth2.config.security.FeignSecurityConfigurer;
import top.wilsonlv.jaguar.oauth2.model.SecurityAuthority;
import top.wilsonlv.jaguar.oauth2.model.SecurityUser;

import java.io.Serializable;
import java.util.Collections;

/**
 * @author lvws
 * @since 2021/12/2
 */
@Component
@RequiredArgsConstructor
@ConditionalOnMissingBean(AuthorizationServerConfigurerAdapter.class)
public class RedisResourceServerServiceImpl implements UserDetailsService {

    private final RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public SecurityUser loadUserByUsername(String serverId) throws UsernameNotFoundException {
        BoundValueOperations<String, Serializable> operations =
                redisTemplate.boundValueOps(Oauth2Constant.RESOURCE_SERVER_CACHE_KEY_PREFIX + serverId);
        Serializable resourceServer = operations.get();
        if (resourceServer == null) {
            throw new UsernameNotFoundException("无效的serverId：" + serverId);
        }

        SecurityUser securityUser = (SecurityUser) resourceServer;
        securityUser.setAuthorities(Collections.singleton(
                new SecurityAuthority(FeignSecurityConfigurer.FEIGN_PERMISSION)));
        return securityUser;
    }

}
