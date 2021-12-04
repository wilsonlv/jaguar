package top.wilsonlv.jaguar.oauth2.component;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Component;
import top.wilsonlv.jaguar.oauth2.Oauth2Constant;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2021/6/30
 */
@Primary
@Component
@RequiredArgsConstructor
public class RedisClientDetailsServiceImpl implements ClientDetailsService {

    private final RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        BoundValueOperations<String, Serializable> operations =
                redisTemplate.boundValueOps(Oauth2Constant.CLIENT_CACHE_KEY_PREFIX + clientId);
        Serializable client = operations.get();
        if (client == null) {
            throw new ClientRegistrationException("无效的clientId：" + clientId);
        }

        return (ClientDetails) client;
    }

}
