package top.wilsonlv.jaguar.cloud.upms.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.klock.annotation.Klock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.wilsonlv.jaguar.basecrud.Assert;
import top.wilsonlv.jaguar.cloud.upms.constant.LockNameConstant;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.OauthClientCreateDTO;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.OauthClientModifyDTO;
import top.wilsonlv.jaguar.cloud.upms.entity.OauthClient;
import top.wilsonlv.jaguar.cloud.upms.entity.ResourceServer;
import top.wilsonlv.jaguar.cloud.upms.mapper.ClientMapper;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.OauthClientVO;
import top.wilsonlv.jaguar.cloud.upms.util.OauthClientUtil;
import top.wilsonlv.jaguar.commons.data.encryption.util.EncryptionUtil;
import top.wilsonlv.jaguar.oauth2.Oauth2Constant;
import top.wilsonlv.jaguar.rediscache.AbstractRedisCacheService;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/6/29
 */
@Service
@RequiredArgsConstructor
public class OauthClientService extends AbstractRedisCacheService<OauthClient, ClientMapper> implements InitializingBean {

    private final RedisTemplate<String, Serializable> redisTemplate;

    private final PasswordEncoder passwordEncoder;

    private final ResourceServerService resourceServerService;

    @Override
    public void afterPropertiesSet() {
        Set<String> keys = redisTemplate.keys(Oauth2Constant.CLIENT_CACHE_KEY_PREFIX + "*");
        if (!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }

        List<OauthClient> oauthClients = this.list(Wrappers.emptyWrapper());
        for (OauthClient oauthClient : oauthClients) {
            this.updateCache(oauthClient);
        }
    }

    public void updateCache(OauthClient oauthClient) {
        OauthClientVO oauthClientVO = OauthClientUtil.entity2VO(oauthClient);
        oauthClientVO.setRegisteredRedirectUri(new HashSet<>());

        if (oauthClientVO.getResourceIds() != null) {
            for (String resourceId : oauthClientVO.getResourceIds()) {
                ResourceServer resourceServer = resourceServerService.getByServerId(resourceId);
                if (resourceServer.getServerMenu()) {
                    oauthClientVO.getRegisteredRedirectUri().add(resourceServer.getServerUrl());
                }
            }
        }

        String key = Oauth2Constant.CLIENT_CACHE_KEY_PREFIX + oauthClient.getClientId();
        redisTemplate.boundValueOps(key).set(oauthClientVO);
    }

    public void deleteCache(String clientId) {
        String key = Oauth2Constant.CLIENT_CACHE_KEY_PREFIX + clientId;
        redisTemplate.delete(key);
    }

    public OauthClient getByClientId(String clientId) {
        return this.unique(Wrappers.lambdaQuery(OauthClient.class)
                .eq(OauthClient::getClientId, clientId));
    }


    public OauthClientVO getDetail(Long id) {
        OauthClient oauthClient = this.getCache(id);
        return oauthClient.toVo(OauthClientVO.class);
    }

    public Page<OauthClientVO> queryOauthClient(Page<OauthClient> page, LambdaQueryWrapper<OauthClient> wrapper) {
        page = this.query(page, wrapper);
        Page<OauthClientVO> voPage = this.toVoPage(page);

        for (OauthClient oauthClient : page.getRecords()) {
            OauthClientVO oauthClientVO = OauthClientUtil.entity2VO(oauthClient);
            voPage.getRecords().add(oauthClientVO);
        }
        return voPage;
    }

    @Klock(name = LockNameConstant.OAUTH_CLIENT_CREATE_MODIFY_LOCK)
    @Transactional
    public String create(OauthClientCreateDTO createDTO) {
        OauthClient byClientId = this.getByClientId(createDTO.getClientId());
        Assert.duplicate(byClientId, "客户端ID");

        String randomPassword = EncryptionUtil.randomPassword(8, 8, 8);
        String encode = passwordEncoder.encode(randomPassword);

        OauthClient oauthClient = OauthClientUtil.dto2Entity(createDTO);
        oauthClient.setClientSecret(encode);
        oauthClient.setDeleted(false);
        this.insert(oauthClient);

        this.afterTransactionCommit(this::updateCache, oauthClient);

        return randomPassword;
    }

    @Klock(name = LockNameConstant.OAUTH_CLIENT_CREATE_MODIFY_LOCK)
    @Transactional
    public void modify(OauthClientModifyDTO modifyDTO) {
        OauthClient byClientId = this.getByClientId(modifyDTO.getClientId());
        Assert.duplicate(byClientId, modifyDTO, "客户端ID");

        OauthClient oauthClient = OauthClientUtil.dto2Entity(modifyDTO);
        oauthClient.setId(modifyDTO.getId());
        this.updateById(oauthClient);

        if (modifyDTO.getEnable()) {
            this.afterTransactionCommit(this::updateCache, getById(modifyDTO.getId()));
        } else {
            this.afterTransactionCommit(this::deleteCache, modifyDTO.getClientId());
        }
    }

    @Transactional
    public String resetSecret(Long id) {
        String randomPassword = EncryptionUtil.randomPassword(8, 8, 8);
        String encode = passwordEncoder.encode(randomPassword);

        OauthClient oauthClient = new OauthClient();
        oauthClient.setId(id);
        oauthClient.setClientSecret(encode);
        this.updateById(oauthClient);

        this.afterTransactionCommit(this::updateCache, getById(id));

        return randomPassword;
    }

    @Transactional
    public void checkAndDelete(Long id) {
        String clientId = this.getById(id).getClientId();
        this.delete(id);
        this.afterTransactionCommit(this::deleteCache, clientId);
    }

}
