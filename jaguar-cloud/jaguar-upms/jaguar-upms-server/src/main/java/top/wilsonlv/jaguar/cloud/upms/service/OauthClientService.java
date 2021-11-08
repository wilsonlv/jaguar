package top.wilsonlv.jaguar.cloud.upms.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.klock.annotation.Klock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wilsonlv.jaguar.cloud.upms.constant.LockNameConstant;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.OauthClientCreateDTO;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.OauthClientModifyDTO;
import top.wilsonlv.jaguar.cloud.upms.entity.OauthClient;
import top.wilsonlv.jaguar.cloud.upms.mapper.ClientMapper;
import top.wilsonlv.jaguar.cloud.upms.sdk.dto.JaguarClientDetails;
import top.wilsonlv.jaguar.cloud.upms.sdk.dto.OauthClientAdditionalInfo;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.OauthClientVO;
import top.wilsonlv.jaguar.commons.basecrud.Assert;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;
import top.wilsonlv.jaguar.commons.oauth2.Oauth2Constant;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityAuthority;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;

import java.io.Serializable;
import java.util.*;

/**
 * @author lvws
 * @since 2021/6/29
 */
@Service
@RequiredArgsConstructor
public class OauthClientService extends BaseService<OauthClient, ClientMapper> implements InitializingBean {

    private final RedisTemplate<String, Serializable> redisTemplate;

    //    clientId              原密码                       密码
    //    jaguar-auth           PHn8KG0T06i45jetPS9ejcT7    $2a$10$kvgD.8bKaY31eAhH/p2qM.5iwP6sxBmTExdMKx.U.kXAZalq.Egsi
    //    jaguar-upms           qb68F1s9YHl9mc1nWJPZ44Uu    $2a$10$gmE9X0d0F7cI2y3tMq8g5uH86mhYCBr3Wcbj4huVm9U0FqSoGFimS
    //    jaguar-websocket      F34ag14gI5UYLJ8U0lhgHo3m    $2a$10$ssSInpunW4K5NllSYT7oA.zQ0ny9ijtkPcsKJJbvD5/vj9GvitPCi
    //    jaguar-handler-log    lJ1MJ80Kmm1oU6kx0W0RCb2b    $2a$10$s6l9eDccvLajyhUvbpdHHOipIh3nCGinyBkDedc4.IXkT8h/lyVXW
    //    jaguar-admin-pc       Q7b6VK0B8j3y4wf5I4oVNfZy    $2a$10$94CLjZ98IRNWzEkJubfIk.rr3DS7YJnqpCiHUSNDGmx2q.xcQsBcG
    //    thirdParty            ygF4Xq8NONr326zC60fzJZ4h    $2a$10$x.DmCRCV.hljeFjQUAIXJOnjm9xan4EgoPPTNZAczQYEWOzo53vIS

    private ClientDetails entity2Dto(OauthClient oauthClient) {
        JaguarClientDetails clientDetails = new JaguarClientDetails();
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
        Set<String> keys = redisTemplate.keys(Oauth2Constant.CLIENT_CACHE_KEY_PREFIX + "*");
        redisTemplate.delete(keys);

        List<OauthClient> oauthClients = this.list(Wrappers.lambdaQuery(OauthClient.class));
        for (OauthClient oauthClient : oauthClients) {
            this.add2Cache(oauthClient);
        }
    }

    public void add2Cache(OauthClient oauthClient) {
        OauthClientAdditionalInfo additionalInfo = OauthClientAdditionalInfo.parse(oauthClient.getAdditionalInformation());
        String key = Oauth2Constant.CLIENT_CACHE_KEY_PREFIX + oauthClient.getClientId();

        if (additionalInfo.getEnable()) {
            ClientDetails clientDetails = entity2Dto(oauthClient);
            redisTemplate.boundValueOps(key).set(clientDetails);
        } else {
            redisTemplate.delete(key);
        }
    }

    public OauthClient getByClientId(String clientId) {
        return this.unique(Wrappers.lambdaQuery(OauthClient.class)
                .eq(OauthClient::getClientId, clientId));
    }

    public OauthClientVO getDetail(Long id) {
        OauthClient oauthClient = this.getById(id);
        return oauthClient.toVo(OauthClientVO.class);
    }

    public Page<OauthClientVO> queryOauthClient(Page<OauthClient> page, LambdaQueryWrapper<OauthClient> wrapper) {
        page = this.query(page, wrapper);
        Page<OauthClientVO> voPage = this.toVoPage(page);

        for (OauthClient oauthClient : page.getRecords()) {
            OauthClientVO oauthClientVO = oauthClient.toVo(OauthClientVO.class);
            voPage.getRecords().add(oauthClientVO);
        }
        return voPage;
    }

    @Klock(name = LockNameConstant.OAUTH_CLIENT_CREATE_MODIFY_LOCK)
    @Transactional
    public void create(OauthClientCreateDTO createDTO) {
        OauthClient byClientId = this.getByClientId(createDTO.getClientId());
        Assert.duplicate(byClientId, "客户端ID");

        OauthClient oauthClient = createDTO.toEntity(OauthClient.class);
        this.insert(oauthClient);

        this.afterTransactionCommit(this::add2Cache, oauthClient);
    }

    @Klock(name = LockNameConstant.OAUTH_CLIENT_CREATE_MODIFY_LOCK)
    @Transactional
    public void modify(OauthClientModifyDTO modifyDTO) {
        this.checkBuiltIn(modifyDTO.getId());

        OauthClient byClientId = this.getByClientId(modifyDTO.getClientId());
        Assert.duplicate(byClientId, modifyDTO, "客户端ID");

        OauthClient oauthClient = modifyDTO.toEntity(OauthClient.class);
        this.updateById(oauthClient);

        this.afterTransactionCommit(this::add2Cache, oauthClient);
    }

    @Klock(name = LockNameConstant.OAUTH_CLIENT_CREATE_MODIFY_LOCK)
    @Transactional
    public void checkAndDelete(Long id) {
        OauthClient oauthClient = this.checkBuiltIn(id);
        this.delete(id);

        String key = Oauth2Constant.CLIENT_CACHE_KEY_PREFIX + oauthClient.getClientId();
        this.afterTransactionCommit(redisTemplate::delete, key);
    }

    public OauthClient checkBuiltIn(Long id) {
        OauthClient oauthClient = this.getById(id);
        OauthClientAdditionalInfo additionalInfo = OauthClientAdditionalInfo.parse(oauthClient.getAdditionalInformation());
        if (additionalInfo.getBuiltIn()) {
            throw new CheckedException("内置oauth客户端不可删除");
        }
        return oauthClient;
    }


}
