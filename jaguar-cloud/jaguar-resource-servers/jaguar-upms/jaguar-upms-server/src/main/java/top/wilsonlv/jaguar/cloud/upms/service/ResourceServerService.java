package top.wilsonlv.jaguar.cloud.upms.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.ResourceServerCreateDTO;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.ResourceServerModifyDTO;
import top.wilsonlv.jaguar.cloud.upms.entity.ResourceServer;
import top.wilsonlv.jaguar.cloud.upms.mapper.ResourceServerMapper;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.ResourceServerVO;
import top.wilsonlv.jaguar.commons.basecrud.Assert;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;
import top.wilsonlv.jaguar.commons.data.encryption.util.EncryptionUtil;
import top.wilsonlv.jaguar.commons.oauth2.Oauth2Constant;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityUser;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 资源服务 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2021-12-02
 */
@Service
@RequiredArgsConstructor
public class ResourceServerService extends BaseService<ResourceServer, ResourceServerMapper> implements InitializingBean {

    private final RedisTemplate<String, Serializable> redisTemplate;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void afterPropertiesSet() {
        Set<String> keys = redisTemplate.keys(Oauth2Constant.RESOURCE_SERVER_CACHE_KEY_PREFIX + "*");
        if (!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }

        List<ResourceServer> resourceServers = this.list(Wrappers.emptyWrapper());
        for (ResourceServer resourceServer : resourceServers) {
            this.updateCache(resourceServer);
        }
    }

    public void updateCache(ResourceServer resourceServer) {
        String key = Oauth2Constant.RESOURCE_SERVER_CACHE_KEY_PREFIX + resourceServer.getServerId();
        SecurityUser securityUser = new SecurityUser();
        securityUser.setId(resourceServer.getId());
        securityUser.setUsername(resourceServer.getServerId());
        securityUser.setPassword(resourceServer.getServerSecret());
        securityUser.setNickName(resourceServer.getServerName());

        JSONObject remarkJson = new JSONObject();
        remarkJson.put("serverMenu", resourceServer.getServerMenu());
        remarkJson.put("serverUrl", resourceServer.getServerUrl());
        securityUser.setRemark(remarkJson.toJSONString());

        redisTemplate.boundValueOps(key).set(securityUser);
    }

    public void deleteCache(String serverId) {
        String key = Oauth2Constant.RESOURCE_SERVER_CACHE_KEY_PREFIX + serverId;
        redisTemplate.delete(key);
    }


    public ResourceServer getByServerId(String serverId) {
        String key = Oauth2Constant.RESOURCE_SERVER_CACHE_KEY_PREFIX + serverId;
        SecurityUser securityUser = (SecurityUser) redisTemplate.boundValueOps(key).get();

        if (securityUser == null) {
            return null;
        }

        ResourceServer resourceServer = JSONObject.parseObject(securityUser.getRemark(), ResourceServer.class);
        resourceServer.setId(securityUser.getId());
        resourceServer.setServerId(securityUser.getUsername());
        resourceServer.setServerName(securityUser.getNickName());
        resourceServer.setServerSecret(securityUser.getPassword());
        return resourceServer;
    }


    public ResourceServerVO getDetail(Long id) {
        ResourceServer resourceServer = this.getById(id);
        return resourceServer.toVo(ResourceServerVO.class);
    }

    public Page<ResourceServerVO> queryResourceServer(Page<ResourceServer> page, LambdaQueryWrapper<ResourceServer> wrapper) {
        page = this.query(page, wrapper);
        Page<ResourceServerVO> voPage = this.toVoPage(page);

        for (ResourceServer resourceServer : page.getRecords()) {
            ResourceServerVO resourceServerVO = resourceServer.toVo(ResourceServerVO.class);
            voPage.getRecords().add(resourceServerVO);
        }
        return voPage;
    }

    @Transactional
    public String create(ResourceServerCreateDTO createDTO) {
        ResourceServer byServerId = this.getByServerId(createDTO.getServerId());
        Assert.duplicate(byServerId, "服务ID");

        String randomPassword = EncryptionUtil.randomPassword(8, 8, 8);
        String encode = passwordEncoder.encode(randomPassword);

        ResourceServer resourceServer = createDTO.toEntity(ResourceServer.class);
        resourceServer.setServerSecret(encode);
        this.insert(resourceServer);

        this.afterTransactionCommit(this::updateCache, resourceServer);

        return randomPassword;
    }

    @Transactional
    public void modify(ResourceServerModifyDTO modifyDTO) {
        ResourceServer byServerId = this.getByServerId(modifyDTO.getServerId());
        Assert.duplicate(byServerId, modifyDTO, "服务ID");

        ResourceServer resourceServer = modifyDTO.toEntity(ResourceServer.class);
        this.updateById(resourceServer);

        this.afterTransactionCommit(this::updateCache, getById(modifyDTO.getId()));
    }

    @Transactional
    public String resetSecret(Long id) {
        String randomPassword = EncryptionUtil.randomPassword(8, 8, 8);
        String encode = passwordEncoder.encode(randomPassword);

        ResourceServer resourceServer = new ResourceServer();
        resourceServer.setId(id);
        resourceServer.setServerSecret(encode);
        this.updateById(resourceServer);

        this.afterTransactionCommit(this::updateCache, getById(id));

        return randomPassword;
    }

    @Transactional
    public void checkAndDelete(Long id) {
        ResourceServer resourceServer = this.getById(id);
        this.delete(id);
        this.afterTransactionCommit(this::deleteCache, resourceServer.getServerId());
    }


}
