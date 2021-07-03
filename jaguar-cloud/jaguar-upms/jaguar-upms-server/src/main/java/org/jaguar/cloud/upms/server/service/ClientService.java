package org.jaguar.cloud.upms.server.service;

import lombok.RequiredArgsConstructor;
import org.jaguar.commons.oauth2.Oauth2Constant;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/6/29
 */
@Service
@RequiredArgsConstructor
public class ClientService implements InitializingBean {

    private final PasswordEncoder passwordEncoder;

    private final RedisTemplate<String, Serializable> redisTemplate;

    public BaseClientDetails loadClientByClientId(String clientId) {
        Set<String> authorizedGrantTypes = new HashSet<>();
        authorizedGrantTypes.add("refresh_token");
        authorizedGrantTypes.add("authorization_code");
        authorizedGrantTypes.add("password");

        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientId(clientId);
        clientDetails.setClientSecret(passwordEncoder.encode("123456"));
        clientDetails.setAuthorizedGrantTypes(authorizedGrantTypes);
        clientDetails.setRegisteredRedirectUri(Collections.singleton("http://localhost:8081"));
        clientDetails.setAccessTokenValiditySeconds(3600);
        clientDetails.setRefreshTokenValiditySeconds(3600 * 24 * 7);
        clientDetails.setScope(Arrays.asList("个人信息", "全部信息"));
        return clientDetails;
    }

    @Override
    public void afterPropertiesSet() {
        String[] clientIds = new String[]{"upms", "auth", "websocket", "job-admin", "job-executor"};

        for (String clientId : clientIds) {
            BoundValueOperations<String, Serializable> operations =
                    redisTemplate.boundValueOps(Oauth2Constant.CLIENT_CACHE_KEY_PREFIX + clientId);
            operations.set(loadClientByClientId(clientId));
        }
    }
}
