package top.wilsonlv.jaguar.cloud.upms.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2021/7/26
 */
@Data
@ApiModel
public class OauthClientCreateDTO implements Serializable {

    @ApiModelProperty("客户端ID")
    private String clientId;

    @ApiModelProperty("客户端密钥")
    private String clientSecret;

    @ApiModelProperty("授权范围")
    private String scope;

    @ApiModelProperty("资源ID")
    private String resourceIds;

    @ApiModelProperty("授权类型")
    private String authorizedGrantTypes;

    @ApiModelProperty("重定向URI")
    private String registeredRedirectUris;

    @ApiModelProperty("自动授权")
    private String autoApproveScopes;

    @ApiModelProperty("权限")
    private String authorities;

    @ApiModelProperty("accessToken有效期")
    private Integer accessTokenValiditySeconds;

    @ApiModelProperty("refreshToken有效期")
    private Integer refreshTokenValiditySeconds;

    @ApiModelProperty("其他信息")
    private String additionalInformation;

}
