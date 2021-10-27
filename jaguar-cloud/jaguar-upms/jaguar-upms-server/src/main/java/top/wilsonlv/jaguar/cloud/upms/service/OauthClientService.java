package top.wilsonlv.jaguar.cloud.upms.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Service;
import top.wilsonlv.jaguar.cloud.upms.mapper.ClientMapper;
import top.wilsonlv.jaguar.cloud.upms.entity.OauthClient;
import top.wilsonlv.jaguar.cloud.upms.sdk.dto.ClientDetailsImpl;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;
import top.wilsonlv.jaguar.commons.data.encryption.util.EncryptionUtil;
import top.wilsonlv.jaguar.commons.oauth2.Oauth2Constant;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityAuthority;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/6/29
 */
@Service
@RequiredArgsConstructor
public class OauthClientService extends BaseService<OauthClient, ClientMapper> implements InitializingBean {

    private final PasswordEncoder passwordEncoder;

    private final RedisTemplate<String, Serializable> redisTemplate;

    public OauthClient loadClientByClientId(String clientId) {
        return this.unique(Wrappers.<OauthClient>lambdaQuery()
                .eq(OauthClient::getClientId, clientId));
    }

    private ClientDetails oauthClient2ClientDetails(OauthClient oauthClient) {
        ClientDetailsImpl clientDetails = new ClientDetailsImpl();
        clientDetails.setClientId(oauthClient.getClientId());
        clientDetails.setClientSecret(oauthClient.getClientSecret());
        clientDetails.setAccessTokenValiditySeconds(oauthClient.getAccessTokenValiditySeconds());
        clientDetails.setRefreshTokenValiditySeconds(oauthClient.getRefreshTokenValiditySeconds());

        if (StringUtils.isNotBlank(oauthClient.getAutoApproveScopes())) {
            clientDetails.setAutoApproveScopes(Arrays.asList(oauthClient.getAutoApproveScopes().split(",")));
        }
        if (StringUtils.isNotBlank(oauthClient.getScope())) {
            clientDetails.setScope(new HashSet<>(Arrays.asList(oauthClient.getScope().split(","))));
        }
        if (StringUtils.isNotBlank(oauthClient.getResourceIds())) {
            clientDetails.setResourceIds(new HashSet<>(Arrays.asList(oauthClient.getResourceIds().split(","))));
        }
        if (StringUtils.isNotBlank(oauthClient.getAuthorizedGrantTypes())) {
            clientDetails.setAuthorizedGrantTypes(new HashSet<>(Arrays.asList(oauthClient.getAuthorizedGrantTypes().split(","))));
        }
        if (StringUtils.isNotBlank(oauthClient.getRegisteredRedirectUris())) {
            clientDetails.setRegisteredRedirectUri(new HashSet<>(Arrays.asList(oauthClient.getRegisteredRedirectUris().split(","))));
        }
        if (StringUtils.isNotBlank(oauthClient.getAuthorities())) {
            String[] authorities = oauthClient.getAuthorities().split(",");

            Set<GrantedAuthority> securityAuthorities = new HashSet<>(authorities.length);
            for (String authority : authorities) {
                securityAuthorities.add(new SecurityAuthority(authority));
            }
            clientDetails.setAuthorities(securityAuthorities);
        }

        clientDetails.setAdditionalInformation(JSONObject.parseObject(oauthClient.getAdditionalInformation()));
        return clientDetails;
    }

    @Override
    public void afterPropertiesSet() {
        List<OauthClient> oauthClients = this.list(Wrappers.lambdaQuery(OauthClient.class)
                .eq(OauthClient::getClientEnable, true));
        for (OauthClient oauthClient : oauthClients) {
            BoundValueOperations<String, Serializable> operations =
                    redisTemplate.boundValueOps(Oauth2Constant.CLIENT_CACHE_KEY_PREFIX + oauthClient.getClientId());

            ClientDetails clientDetails = oauthClient2ClientDetails(oauthClient);
            operations.set(clientDetails);
        }
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        for (int i = 0; i < 1; i++) {
            String password = EncryptionUtil.randomPassword(8, 8, 8);
            String encode = encoder.encode(password);
            System.out.println(password);
            System.out.println(encode);
            System.out.println(IdWorker.getId());
        }
    }

//    clientId              原密码                       密码
//    jaguar-auth           PHn8KG0T06i45jetPS9ejcT7    $2a$10$kvgD.8bKaY31eAhH/p2qM.5iwP6sxBmTExdMKx.U.kXAZalq.Egsi
//    jaguar-upms           qb68F1s9YHl9mc1nWJPZ44Uu    $2a$10$gmE9X0d0F7cI2y3tMq8g5uH86mhYCBr3Wcbj4huVm9U0FqSoGFimS
//    jaguar-websocket      F34ag14gI5UYLJ8U0lhgHo3m    $2a$10$ssSInpunW4K5NllSYT7oA.zQ0ny9ijtkPcsKJJbvD5/vj9GvitPCi
//    jaguar-handler-log    lJ1MJ80Kmm1oU6kx0W0RCb2b    $2a$10$s6l9eDccvLajyhUvbpdHHOipIh3nCGinyBkDedc4.IXkT8h/lyVXW
//    jaguar-admin-pc       Q7b6VK0B8j3y4wf5I4oVNfZy    $2a$10$94CLjZ98IRNWzEkJubfIk.rr3DS7YJnqpCiHUSNDGmx2q.xcQsBcG
//    thirdParty            ygF4Xq8NONr326zC60fzJZ4h    $2a$10$x.DmCRCV.hljeFjQUAIXJOnjm9xan4EgoPPTNZAczQYEWOzo53vIS

}
