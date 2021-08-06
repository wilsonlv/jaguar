package top.wilsonlv.jaguar.cloud.upms.model;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.basecrud.BaseModel;
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
@TableName("jaguar_cloud_upms_oauth_client")
public class OauthClient extends BaseModel {

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

}
