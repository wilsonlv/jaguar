package top.wilsonlv.jaguar.commons.oauth2.model;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
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

    private String phone;

    private String email;

    private Boolean locked = false;

    private Boolean enable = true;

    private LocalDateTime passwordLastModifyTime;

    private Collection<SecurityAuthority> authorities;


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.passwordLastModifyTime == null || LocalDateTime.now().plusDays(180).isAfter(this.passwordLastModifyTime);
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }

}
