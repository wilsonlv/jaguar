package top.wilsonlv.jaguar.cloud.upms.sdk.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/8/6
 */
@Data
public class JaguarClientDetails implements ClientDetails {

    private String clientId;

    private String clientSecret;

    private Set<String> scope = Collections.emptySet();

    private Set<String> resourceIds;

    private Set<String> authorizedGrantTypes;

    private Set<String> registeredRedirectUri;

    private Collection<String> autoApproveScopes;

    private Collection<GrantedAuthority> authorities = Collections.emptySet();

    private Integer accessTokenValiditySeconds;

    private Integer refreshTokenValiditySeconds;

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
