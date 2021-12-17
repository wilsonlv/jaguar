package top.wilsonlv.jaguar.cloud.upms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.cloud.upms.sdk.enums.ClientType;
import top.wilsonlv.jaguar.cloud.upms.sdk.enums.UserType;
import top.wilsonlv.jaguar.datamodifylog.entity.DataModifyLoggable;

/**
 * @author lvws
 * @since 2021/7/26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("jaguar_cloud_upms_oauth_client")
public class OAuthClient extends DataModifyLoggable {

    /**
     * 客户端ID
     */
    @TableField("client_id")
    private String clientId;
    /**
     * 客户端密钥
     */
    @TableField("client_secret")
    private String clientSecret;
    /**
     * 资源ID
     */
    @TableField("resource_ids")
    private String resourceIds;
    /**
     * 是否第三方
     */
    @TableField("third_party")
    private Boolean thirdParty;
    /**
     * 授权类型（authorization_code,password,refresh_token,implicit,client_credentials）
     */
    @TableField("authorized_grant_types")
    private String authorizedGrantTypes;
    /**
     * 重定向URI
     */
    @TableField("registered_redirect_uri")
    private String registeredRedirectUri;
    /**
     * 权限范围
     */
    @TableField("scope_")
    private String scope;
    /**
     * 自动授权范围
     */
    @TableField("auto_approve_scopes")
    private String autoApproveScopes;
    /**
     * accessToken有效期
     */
    @TableField("access_token_validity_seconds")
    private Integer accessTokenValiditySeconds;
    /**
     * refreshToken有效期
     */
    @TableField("refresh_token_validity_seconds")
    private Integer refreshTokenValiditySeconds;
    /**
     * 客户端类型
     */
    @TableField("client_type")
    private ClientType clientType;
    /**
     * 用户类型
     */
    @TableField("user_type")
    private UserType userType;
    /**
     * 是否启用
     */
    @TableField("enable_")
    private Boolean enable;

}
