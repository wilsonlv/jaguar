package org.jaguar.cloud.upms.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.jaguar.cloud.upms.mapper.ClientMapper;
import org.jaguar.cloud.upms.model.Client;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.commons.oauth2.Oauth2Constant;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author lvws
 * @since 2021/6/29
 */
@Service
@RequiredArgsConstructor
public class ClientService extends BaseService<Client, ClientMapper> implements InitializingBean {

    private final PasswordEncoder passwordEncoder;

    private final RedisTemplate<String, Serializable> redisTemplate;

    //        Set<String> authorizedGrantTypes = new HashSet<>();
//        authorizedGrantTypes.add("refresh_token");
//        authorizedGrantTypes.add("authorization_code");
//        authorizedGrantTypes.add("password");
//
//        BaseClientDetails clientDetails = new BaseClientDetails();
//        clientDetails.setClientId(clientId);
//        clientDetails.setClientSecret(passwordEncoder.encode("123456"));
//        clientDetails.setAuthorizedGrantTypes(authorizedGrantTypes);
//        clientDetails.setRegisteredRedirectUri(Collections.singleton("http://localhost:8081"));
//        clientDetails.setAccessTokenValiditySeconds(3600);
//        clientDetails.setRefreshTokenValiditySeconds(3600 * 24 * 7);
//        clientDetails.setScope(Arrays.asList("个人信息", "全部信息"));
//        clientDetails.setResourceIds(CollectionUtil.newHashSet(
//                "jaguar-upms-server", "jaguar-auth-server", "jaguar-websocket-server",
//                "jaguar-job-admin", "jaguar-job-executor"));
//
//        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("feign");
//        clientDetails.setAuthorities(new HashSet<>(authorities));
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("captcha", false);
//        clientDetails.setAdditionalInformation(params);

    public Client loadClientByClientId(String clientId) {
        return this.unique(Wrappers.<Client>lambdaQuery()
                .eq(Client::getClientId, clientId));
    }

    @Override
    public void afterPropertiesSet() {
        List<Client> clients = this.list(Wrappers.emptyWrapper());
        for (Client client : clients) {
            BoundValueOperations<String, Serializable> operations =
                    redisTemplate.boundValueOps(Oauth2Constant.CLIENT_CACHE_KEY_PREFIX + client.getClientId());
            operations.set(client);
        }
    }

    public static void main(String[] args) {
        System.out.println(IdWorker.getId());
        System.out.println(IdWorker.getId());
        System.out.println(IdWorker.getId());
    }
}
