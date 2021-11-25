package top.wilsonlv.jaguar.cloud.upms.controller.dto;

import top.wilsonlv.jaguar.commons.enums.ClientType;
import top.wilsonlv.jaguar.commons.enums.UserType;

import java.util.Set;

/**
 * @author lvws
 * @since 2021/11/9
 */
public interface OauthClientBaseDTO {

    String getClientId();

    void setClientId(String clientId);

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

    Set<String> getAuthorities();

    void setAuthorities(Set<String> authorities);

    Boolean getEnable();

    void setEnable(Boolean enable);

    ClientType getClientType();

    void setClientType(ClientType clientType);

    UserType getUserType();

    void setUserType(UserType userType);

    Boolean getCaptcha();

    void setCaptcha(Boolean captcha);

}
