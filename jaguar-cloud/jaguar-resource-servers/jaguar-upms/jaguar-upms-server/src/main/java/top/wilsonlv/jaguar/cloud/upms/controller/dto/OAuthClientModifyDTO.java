package top.wilsonlv.jaguar.cloud.upms.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.cloud.upms.sdk.enums.ClientType;
import top.wilsonlv.jaguar.cloud.upms.sdk.enums.UserType;
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
public class OAuthClientModifyDTO extends BaseModifyDTO implements OAuthClientBaseDTO {

    @NotBlank(message = "客户端ID为非空")
    @ApiModelProperty(value = "客户端ID", required = true)
    private String clientId;

    @NotNull(message = "是否第三方为非空")
    @ApiModelProperty(value = "是否第三方", required = true)
    private Boolean thirdParty;

    @ApiModelProperty(value = "授权类型")
    private Set<String> authorizedGrantTypes;

    @NotNull(message = "accessToken有效期为非空")
    @ApiModelProperty(value = "accessToken有效期", required = true)
    private Integer accessTokenValiditySeconds;

    @NotNull(message = "refreshToken有效期为非空")
    @ApiModelProperty(value = "refreshToken有效期", required = true)
    private Integer refreshTokenValiditySeconds;

    @NotEmpty(message = "授权范围为非空")
    @ApiModelProperty(value = "授权范围", required = true)
    private Set<String> scope;

    @ApiModelProperty("自动授权")
    private Set<String> autoApproveScopes;

    @NotEmpty(message = "资源ID为非空")
    @ApiModelProperty(value = "资源ID", required = true)
    private Set<String> resourceIds;

    @ApiModelProperty("重定向URI")
    private Set<String> registeredRedirectUri;

    @NotNull(message = "客户端类型为非空")
    @ApiModelProperty(value = "客户端类型", required = true)
    private ClientType clientType;

    @NotNull(message = "用户类型为非空")
    @ApiModelProperty(value = "用户类型", required = true)
    private UserType userType;

    @NotNull(message = "是否启用为非空")
    @ApiModelProperty(value = "是否启用", required = true)
    private Boolean enable;

}
