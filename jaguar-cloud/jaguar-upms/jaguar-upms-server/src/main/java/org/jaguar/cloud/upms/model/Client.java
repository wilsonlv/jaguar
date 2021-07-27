package org.jaguar.cloud.upms.model;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.commons.basecrud.BaseModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;

/**
 * @author lvws
 * @since 2021/7/26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("jaguar_cloud_upms_client")
public class Client extends BaseModel implements ClientDetails {

    @TableField("client_id")
    private String clientId;

    @TableField("client_secret")
    private String clientSecret;

    @TableField("scope_")
    private String scope;

    @TableField("resource_ids")
    private String resourceIds;

    @TableField("authorized_grant_types")
    private String authorizedGrantTypes;

    @TableField("registered_redirect_uris")
    private String registeredRedirectUris;

    @TableField("auto_approve_scopes")
    private String autoApproveScopes;

    @TableField("authorities_")
    private String authorities;

    @TableField("access_token_validity_seconds")
    private Integer accessTokenValiditySeconds;

    @TableField("refresh_token_validity_seconds")
    private Integer refreshTokenValiditySeconds;

    @TableField("additional_information")
    private String additionalInformation;

    @TableField("client_enable")
    private Boolean clientEnable;


    @Override
    public boolean isSecretRequired() {
        return this.clientSecret != null;
    }

    @Override
    public boolean isScoped() {
        return this.scope != null && !this.scope.isEmpty();
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        if (registeredRedirectUris == null) {
            return Collections.emptySet();
        }
        return new HashSet<>(Arrays.asList(registeredRedirectUris.split(",")));
    }

    @Override
    public boolean isAutoApprove(String scope) {
        if (autoApproveScopes == null) {
            return false;
        }
        for (String auto : autoApproveScopes.split(",")) {
            if ("true".equals(auto) || scope.matches(auto)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<String> getResourceIds() {
        if (resourceIds == null) {
            return Collections.emptySet();
        }
        return new HashSet<>(Arrays.asList(resourceIds.split(",")));
    }

    @Override
    public Set<String> getScope() {
        if (scope == null) {
            return Collections.emptySet();
        }
        return new HashSet<>(Arrays.asList(scope.split(",")));
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        if (authorizedGrantTypes == null) {
            return Collections.emptySet();
        }
        return new HashSet<>(Arrays.asList(authorizedGrantTypes.split(",")));
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            return Collections.emptySet();
        }
        return AuthorityUtils.createAuthorityList(authorities.split(","));
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return JSONObject.parseObject(additionalInformation);
    }
}
