package top.wilsonlv.jaguar.oauth2.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author lvws
 * @since 2021/7/1
 */
@Data
public class SecurityAuthority implements GrantedAuthority {

    private String authority;

    public SecurityAuthority() {
    }

    public SecurityAuthority(String authority) {
        this.authority = authority;
    }
}
