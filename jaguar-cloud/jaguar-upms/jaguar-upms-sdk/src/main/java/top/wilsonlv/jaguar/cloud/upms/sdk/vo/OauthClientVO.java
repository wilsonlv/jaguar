package top.wilsonlv.jaguar.cloud.upms.sdk.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import top.wilsonlv.jaguar.commons.web.base.BaseVO;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/8/6
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class OauthClientVO extends BaseVO implements ClientDetails {

    @ApiModelProperty("客户端ID")
    private String clientId;

    @ApiModelProperty("客户端密钥")
    private String clientSecret;

    @ApiModelProperty("权限范围")
    private Set<String> scope = Collections.emptySet();

    @ApiModelProperty("资源ID")
    private Set<String> resourceIds;

    @ApiModelProperty("授权类型")
    private Set<String> authorizedGrantTypes;

    @ApiModelProperty("重定向URI")
    private Set<String> registeredRedirectUri;

    @ApiModelProperty("自动授权")
    private Collection<String> autoApproveScopes;

    @ApiModelProperty("权限")
    private Collection<GrantedAuthority> authorities = Collections.emptySet();

    @ApiModelProperty("accessToken有效期")
    private Integer accessTokenValiditySeconds;

    @ApiModelProperty("refreshToken有效期")
    private Integer refreshTokenValiditySeconds;

    @ApiModelProperty("其他信息")
    private Map<String, Object> additionalInformation;

    @Override
    @JsonIgnore
    public boolean isSecretRequired() {
        return this.clientSecret != null;
    }

    @Override
    @JsonIgnore
    public boolean isScoped() {
        return this.scope != null && !this.scope.isEmpty();
    }

    @Override
    @JsonIgnore
    public boolean isAutoApprove(String scope) {
        if (autoApproveScopes == null) {
            return false;
        }
        for (String auto : autoApproveScopes) {
            if ("true".equals(auto) || scope.matches(auto)) {
                return true;
            }
        }
        return false;
    }

}
