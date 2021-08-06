package top.wilsonlv.jaguar.commons.oauth2.component;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import top.wilsonlv.jaguar.commons.oauth2.Oauth2Constant;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;

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
