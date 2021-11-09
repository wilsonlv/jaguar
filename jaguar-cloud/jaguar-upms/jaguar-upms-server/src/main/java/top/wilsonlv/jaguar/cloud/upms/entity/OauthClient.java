package top.wilsonlv.jaguar.cloud.upms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.basecrud.BaseModel;
import top.wilsonlv.jaguar.commons.enums.ClientType;
import top.wilsonlv.jaguar.commons.enums.UserType;

/**
 * @author lvws
 * @since 2021/7/26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("jaguar_cloud_upms_oauth_client")
public class OauthClient extends BaseModel {

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
     * 权限范围
     */
    @TableField("scope_")
    private String scope;
    /**
     * 资源ID
     */
    @TableField("resource_ids")
    private String resourceIds;
    /**
     * 授权类型（authorization_code,password,refresh_token,implicit,client_credentials）
     */
    @TableField("authorized_grant_types")
    private String authorizedGrantTypes;
    /**
     * 重定向URI
     */
    @TableField("registered_redirect_uris")
    private String registeredRedirectUris;
    /**
     * 自动授权
     */
    @TableField("auto_approve_scopes")
    private String autoApproveScopes;
    /**
     * 权限
     */
    @TableField("authorities_")
    private String authorities;
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
     * 其他信息
     */
    @TableField("additional_information")
    private String additionalInformation;
    /**
     * 是否内置
     */
    @TableField("built_in")
    private Boolean builtIn;
    /**
     * 是否启用
     */
    @TableField("enable_")
    private Boolean enable;
    /**
     * 是否需要验证码
     */
    @TableField("captcha_")
    private Boolean captcha;
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

}
