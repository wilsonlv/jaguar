package top.wilsonlv.jaguar.commons.oauth2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author lvws
 * @since 2021/6/22
 */
@Data
public class SecurityUser implements UserDetails {

    private Long id;

    private Long tenantId;

    private String username;

    private String password;

    private String nickName;

    private Boolean credentialsNonExpired = true;

    private Boolean accountNonLocked = true;

    private Boolean enabled = true;

    private Collection<SecurityAuthority> authorities;

    private String remark;


    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return this.enabled;
    }

}
