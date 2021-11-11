package top.wilsonlv.jaguar.cloud.upms.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.enums.ClientType;
import top.wilsonlv.jaguar.commons.enums.UserType;
import top.wilsonlv.jaguar.commons.web.base.BaseModifyDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/7/26
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class OauthClientModifyDTO extends BaseModifyDTO implements OauthClientBaseDTO {

    @NotBlank(message = "客户端ID为非空")
    @ApiModelProperty(value = "客户端ID", required = true)
    private String clientId;

    @ApiModelProperty(value = "授权类型")
    private Set<String> authorizedGrantTypes;

    @NotNull(message = "accessToken有效期为非空")
    @ApiModelProperty(value = "accessToken有效期", required = true)
    private Integer accessTokenValiditySeconds;

    @NotNull(message = "refreshToken有效期为非空")
    @ApiModelProperty(value = "refreshToken有效期", required = true)
    private Integer refreshTokenValiditySeconds;

    @ApiModelProperty("授权范围")
    private Set<String> scope;

    @ApiModelProperty("自动授权")
    private Set<String> autoApproveScopes;

    @ApiModelProperty("资源ID")
    private Set<String> resourceIds;

    @ApiModelProperty("重定向URI")
    private Set<String> registeredRedirectUri;

    @ApiModelProperty("权限")
    private Set<String> authorities;

    @NotNull(message = "客户端类型为非空")
    @ApiModelProperty(value = "客户端类型", required = true)
    private ClientType clientType;

    @ApiModelProperty(value = "用户类型")
    private UserType userType;

    @ApiModelProperty(value = "是否需要验证码")
    private Boolean captcha;

    @NotNull(message = "是否启用为非空")
    @ApiModelProperty(value = "是否启用", required = true)
    private Boolean enable;

}
