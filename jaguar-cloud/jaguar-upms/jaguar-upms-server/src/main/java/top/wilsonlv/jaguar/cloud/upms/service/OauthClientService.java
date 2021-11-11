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
import top.wilsonlv.jaguar.cloud.upms.constant.LockNameConstant;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.OauthClientCreateDTO;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.OauthClientModifyDTO;
import top.wilsonlv.jaguar.cloud.upms.entity.OauthClient;
import top.wilsonlv.jaguar.cloud.upms.mapper.ClientMapper;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.OauthClientVO;
import top.wilsonlv.jaguar.cloud.upms.util.OauthClientUtil;
import top.wilsonlv.jaguar.commons.basecrud.Assert;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;
import top.wilsonlv.jaguar.commons.data.encryption.util.EncryptionUtil;
import top.wilsonlv.jaguar.commons.oauth2.Oauth2Constant;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/6/29
 */
@Service
@RequiredArgsConstructor
public class OauthClientService extends BaseService<OauthClient, ClientMapper> implements InitializingBean {

    private final RedisTemplate<String, Serializable> redisTemplate;

    private final PasswordEncoder passwordEncoder;

    //    clientId              原密码                       密码
    //    jaguar-auth           PHn8KG0T06i45jetPS9ejcT7    $2a$10$kvgD.8bKaY31eAhH/p2qM.5iwP6sxBmTExdMKx.U.kXAZalq.Egsi
    //    jaguar-upms           qb68F1s9YHl9mc1nWJPZ44Uu    $2a$10$gmE9X0d0F7cI2y3tMq8g5uH86mhYCBr3Wcbj4huVm9U0FqSoGFimS
    //    jaguar-websocket      F34ag14gI5UYLJ8U0lhgHo3m    $2a$10$ssSInpunW4K5NllSYT7oA.zQ0ny9ijtkPcsKJJbvD5/vj9GvitPCi
    //    jaguar-handler-log    lJ1MJ80Kmm1oU6kx0W0RCb2b    $2a$10$s6l9eDccvLajyhUvbpdHHOipIh3nCGinyBkDedc4.IXkT8h/lyVXW
    //    jaguar-admin-pc       Q7b6VK0B8j3y4wf5I4oVNfZy    $2a$10$94CLjZ98IRNWzEkJubfIk.rr3DS7YJnqpCiHUSNDGmx2q.xcQsBcG
    //    thirdParty            ygF4Xq8NONr326zC60fzJZ4h    $2a$10$x.DmCRCV.hljeFjQUAIXJOnjm9xan4EgoPPTNZAczQYEWOzo53vIS

    @Override
    public void afterPropertiesSet() {
        Set<String> keys = redisTemplate.keys(Oauth2Constant.CLIENT_CACHE_KEY_PREFIX + "*");
        if (!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }

        List<OauthClient> oauthClients = this.list(Wrappers.lambdaQuery(OauthClient.class));
        for (OauthClient oauthClient : oauthClients) {
            this.add2Cache(oauthClient);
        }
    }

    public void add2Cache(OauthClient oauthClient) {
        String key = Oauth2Constant.CLIENT_CACHE_KEY_PREFIX + oauthClient.getClientId();
        if (oauthClient.getEnable()) {
            OauthClientVO oauthClientVO = OauthClientUtil.entity2VO(oauthClient);
            redisTemplate.boundValueOps(key).set(oauthClientVO);
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
        this.insert(oauthClient);

        this.afterTransactionCommit(this::add2Cache, oauthClient);

        return randomPassword;
    }

    @Klock(name = LockNameConstant.OAUTH_CLIENT_CREATE_MODIFY_LOCK)
    @Transactional
    public void modify(OauthClientModifyDTO modifyDTO) {
        this.checkBuiltIn(modifyDTO.getId());

        OauthClient byClientId = this.getByClientId(modifyDTO.getClientId());
        Assert.duplicate(byClientId, modifyDTO, "客户端ID");

        OauthClient oauthClient = OauthClientUtil.dto2Entity(modifyDTO);
        oauthClient.setId(modifyDTO.getId());
        this.updateById(oauthClient);

        this.afterTransactionCommit(this::add2Cache, getById(modifyDTO.getId()));
    }

    @Transactional
    public String resetSecret(Long id) {
        OauthClient oauthClient = this.checkBuiltIn(id);

        String randomPassword = EncryptionUtil.randomPassword(8, 8, 8);
        String encode = passwordEncoder.encode(randomPassword);

        oauthClient.setClientSecret(encode);
        this.updateById(oauthClient);

        return randomPassword;
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
        if (oauthClient.getBuiltIn()) {
            throw new CheckedException("内置oauth客户端不可修改或删除");
        }
        return oauthClient;
    }

}
