package top.wilsonlv.jaguar.cloud.upms.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.OAuthClientBaseDTO;
import top.wilsonlv.jaguar.cloud.upms.entity.OAuthClient;
import top.wilsonlv.jaguar.cloud.upms.sdk.dto.OAuthClientAdditionalInfo;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.OauthClientVO;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author lvws
 * @since 2021/11/9
 */
public class OAuthClientUtil {

    public static OauthClientVO entity2VO(OAuthClient oauthClient) {
        OauthClientVO oauthClientVO = new OauthClientVO();
        oauthClientVO.setId(oauthClient.getId());
        oauthClientVO.setClientId(oauthClient.getClientId());
        oauthClientVO.setClientSecret(oauthClient.getClientSecret());
        oauthClientVO.setAccessTokenValiditySeconds(oauthClient.getAccessTokenValiditySeconds());
        oauthClientVO.setRefreshTokenValiditySeconds(oauthClient.getRefreshTokenValiditySeconds());

        oauthClientVO.setAuthorizedGrantTypes(new HashSet<>(Arrays.asList(oauthClient.getAuthorizedGrantTypes().split(","))));
        oauthClientVO.setResourceIds(new HashSet<>(Arrays.asList(oauthClient.getResourceIds().split(","))));

        oauthClientVO.setScope(new HashSet<>(Arrays.asList(oauthClient.getScope().split(","))));
        if (StringUtils.hasText(oauthClient.getAutoApproveScopes())) {
            oauthClientVO.setAutoApproveScopes(new HashSet<>(Arrays.asList(oauthClient.getAutoApproveScopes().split(","))));
        }

        if (StringUtils.hasText(oauthClient.getRegisteredRedirectUri())) {
            oauthClientVO.setRegisteredRedirectUri(new HashSet<>(Arrays.asList(oauthClient.getRegisteredRedirectUri().split(","))));
        } else {
            oauthClientVO.setRegisteredRedirectUri(new HashSet<>());
        }

        OAuthClientAdditionalInfo oauthClientAdditionalInfo = new OAuthClientAdditionalInfo();
        BeanUtils.copyProperties(oauthClient, oauthClientAdditionalInfo);
        JSONObject additionalInformation = JSONObject.parseObject(oauthClientAdditionalInfo.toJsonString());
        oauthClientVO.setAdditionalInformation(additionalInformation);

        return oauthClientVO;
    }

    public static OAuthClient dto2Entity(OAuthClientBaseDTO oauthClientDTO) {
        OAuthClient oauthClient = new OAuthClient();
        oauthClient.setClientId(oauthClientDTO.getClientId());
        oauthClient.setAccessTokenValiditySeconds(oauthClientDTO.getAccessTokenValiditySeconds());
        oauthClient.setRefreshTokenValiditySeconds(oauthClientDTO.getRefreshTokenValiditySeconds());
        oauthClient.setThirdParty(oauthClientDTO.getThirdParty());
        oauthClient.setClientType(oauthClientDTO.getClientType());
        oauthClient.setUserType(oauthClientDTO.getUserType());
        oauthClient.setEnable(oauthClientDTO.getEnable());

        oauthClient.setAuthorizedGrantTypes(String.join(",", oauthClientDTO.getAuthorizedGrantTypes()));
        oauthClient.setResourceIds(String.join(",", oauthClientDTO.getResourceIds()));

        oauthClient.setScope(String.join(",", oauthClientDTO.getScope()));
        if (!CollectionUtils.isEmpty(oauthClientDTO.getAutoApproveScopes())) {
            oauthClient.setAutoApproveScopes(String.join(",", oauthClientDTO.getAutoApproveScopes()));
        }
        if (!CollectionUtils.isEmpty(oauthClientDTO.getRegisteredRedirectUri())) {
            oauthClient.setRegisteredRedirectUri(String.join(",", oauthClientDTO.getRegisteredRedirectUri()));
        }
        return oauthClient;
    }

}
