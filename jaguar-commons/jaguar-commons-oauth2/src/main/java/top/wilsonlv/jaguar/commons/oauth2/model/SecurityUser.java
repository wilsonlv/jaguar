package top.wilsonlv.jaguar.commons.oauth2.model;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import top.wilsonlv.jaguar.commons.enums.UserType;

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

    private Boolean buildIn = false;

    private String username;

    private String password;

    private UserType userType;

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
        return this.buildIn || (this.passwordLastModifyTime != null && this.passwordLastModifyTime.plusDays(90).isAfter(LocalDateTime.now()));
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }

}
