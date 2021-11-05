package top.wilsonlv.jaguar.cloud.upms.controller.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.basecrud.BaseModel;
import top.wilsonlv.jaguar.commons.web.base.BaseModifyDTO;

/**
 * @author lvws
 * @since 2021/7/26
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class OauthClientModifyDTO extends BaseModifyDTO {

    @TableField("client_id")
    private String clientId;

    @TableField("client_secret")
    private String clientSecret;

    @TableField("client_built_in")
    private Boolean clientBuiltIn;

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
