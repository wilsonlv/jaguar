package top.wilsonlv.jaguar.cloud.upms.controller.dto;

import top.wilsonlv.jaguar.cloud.upms.sdk.enums.ClientType;
import top.wilsonlv.jaguar.cloud.upms.sdk.enums.UserType;

import java.util.Set;

/**
 * @author lvws
 * @since 2021/11/9
 */
public interface OAuthClientBaseDTO {

    /**
     * 获取客户端ID
     *
     * @return clientId
     */
    String getClientId();

    /**
     * 设置客户端ID
     *
     * @param clientId clientId
     */
    void setClientId(String clientId);

    Boolean getThirdParty();

    void setThirdParty(Boolean thirdParty);

    Set<String> getAuthorizedGrantTypes();

    void setAuthorizedGrantTypes(Set<String> authorizedGrantTypes);

    Integer getAccessTokenValiditySeconds();

    void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds);

    Integer getRefreshTokenValiditySeconds();

    void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds);

    Set<String> getScope();

    void setScope(Set<String> scope);

    Set<String> getAutoApproveScopes();

    void setAutoApproveScopes(Set<String> autoApproveScopes);

    Set<String> getResourceIds();

    void setResourceIds(Set<String> resourceIds);

    Set<String> getRegisteredRedirectUri();

    void setRegisteredRedirectUri(Set<String> registeredRedirectUri);

    ClientType getClientType();

    void setClientType(ClientType clientType);

    UserType getUserType();

    void setUserType(UserType userType);

    Boolean getEnable();

    void setEnable(Boolean enable);

}
