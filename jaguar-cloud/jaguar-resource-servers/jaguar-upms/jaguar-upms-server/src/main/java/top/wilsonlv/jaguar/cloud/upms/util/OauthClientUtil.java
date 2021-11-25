package top.wilsonlv.jaguar.cloud.upms.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.OauthClientBaseDTO;
import top.wilsonlv.jaguar.cloud.upms.entity.OauthClient;
import top.wilsonlv.jaguar.cloud.upms.sdk.dto.OauthClientAdditionalInfo;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.OauthClientVO;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/11/9
 */
public class OauthClientUtil {

    public static OauthClientVO entity2VO(OauthClient oauthClient) {
        OauthClientVO oauthClientVO = new OauthClientVO();
        oauthClientVO.setId(oauthClient.getId());
        oauthClientVO.setClientId(oauthClient.getClientId());
        oauthClientVO.setClientSecret(oauthClient.getClientSecret());
        oauthClientVO.setAccessTokenValiditySeconds(oauthClient.getAccessTokenValiditySeconds());
        oauthClientVO.setRefreshTokenValiditySeconds(oauthClient.getRefreshTokenValiditySeconds());

        if (StringUtils.isNotBlank(oauthClient.getAuthorizedGrantTypes())) {
            oauthClientVO.setAuthorizedGrantTypes(new HashSet<>(Arrays.asList(oauthClient.getAuthorizedGrantTypes().split(","))));
        }
        if (StringUtils.isNotBlank(oauthClient.getScope())) {
            oauthClientVO.setScope(new HashSet<>(Arrays.asList(oauthClient.getScope().split(","))));
        }
        if (StringUtils.isNotBlank(oauthClient.getAutoApproveScopes())) {
            oauthClientVO.setAutoApproveScopes(new HashSet<>(Arrays.asList(oauthClient.getAutoApproveScopes().split(","))));
        }
        if (StringUtils.isNotBlank(oauthClient.getResourceIds())) {
            oauthClientVO.setResourceIds(new HashSet<>(Arrays.asList(oauthClient.getResourceIds().split(","))));
        }
        if (StringUtils.isNotBlank(oauthClient.getRegisteredRedirectUri())) {
            oauthClientVO.setRegisteredRedirectUri(new HashSet<>(Arrays.asList(oauthClient.getRegisteredRedirectUri().split(","))));
        }
        if (StringUtils.isNotBlank(oauthClient.getAuthorities())) {
            String[] authorities = oauthClient.getAuthorities().split(",");

            Set<GrantedAuthority> securityAuthorities = new HashSet<>(authorities.length);
            for (String authority : authorities) {
                securityAuthorities.add(new SecurityAuthority(authority));
            }
            oauthClientVO.setAuthorities(securityAuthorities);
        }

        OauthClientAdditionalInfo oauthClientAdditionalInfo = new OauthClientAdditionalInfo();
        BeanUtils.copyProperties(oauthClient, oauthClientAdditionalInfo);
        JSONObject additionalInformation = JSONObject.parseObject(oauthClientAdditionalInfo.toJSONString());
        oauthClientVO.setAdditionalInformation(additionalInformation);

        return oauthClientVO;
    }

    public static OauthClient dto2Entity(OauthClientBaseDTO oauthClientDTO) {
        OauthClient oauthClient = new OauthClient();
        oauthClient.setClientId(oauthClientDTO.getClientId());
        oauthClient.setAccessTokenValiditySeconds(oauthClientDTO.getAccessTokenValiditySeconds());
        oauthClient.setRefreshTokenValiditySeconds(oauthClientDTO.getRefreshTokenValiditySeconds());
        oauthClient.setEnable(oauthClientDTO.getEnable());
        oauthClient.setCaptcha(oauthClientDTO.getCaptcha());
        oauthClient.setClientType(oauthClientDTO.getClientType());
        oauthClient.setUserType(oauthClientDTO.getUserType());

        if (!CollectionUtils.isEmpty(oauthClientDTO.getAuthorizedGrantTypes())) {
            oauthClient.setAuthorizedGrantTypes(StringUtils.join(oauthClientDTO.getAuthorizedGrantTypes().toArray(), ","));
        }
        if (!CollectionUtils.isEmpty(oauthClientDTO.getScope())) {
            oauthClient.setScope(StringUtils.join(oauthClientDTO.getScope().toArray(), ","));
        }
        if (!CollectionUtils.isEmpty(oauthClientDTO.getAutoApproveScopes())) {
            oauthClient.setAutoApproveScopes(StringUtils.join(oauthClientDTO.getAutoApproveScopes().toArray(), ","));
        }
        if (!CollectionUtils.isEmpty(oauthClientDTO.getResourceIds())) {
            oauthClient.setResourceIds(StringUtils.join(oauthClientDTO.getResourceIds().toArray(), ","));
        }
        if (!CollectionUtils.isEmpty(oauthClientDTO.getRegisteredRedirectUri())) {
            oauthClient.setRegisteredRedirectUri(StringUtils.join(oauthClientDTO.getRegisteredRedirectUri().toArray(), ","));
        }
        if (!CollectionUtils.isEmpty(oauthClientDTO.getAuthorities())) {
            oauthClient.setAuthorities(StringUtils.join(oauthClientDTO.getAuthorities().toArray(), ","));
        }
        return oauthClient;
    }

}
