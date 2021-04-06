package org.jaguar.test.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author lvws
 * @since 2021/4/2
 */
@Data
public class UserAuthority implements GrantedAuthority {

    private String authority;

    public UserAuthority() {
    }

    public UserAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserAuthority that = (UserAuthority) o;

        return authority.equals(that.authority);
    }

    @Override
    public int hashCode() {
        return authority.hashCode();
    }
}
