package org.jaguar.modules.system.mgm.auth;

import org.jaguar.modules.system.mgm.model.User;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author lvws
 * @since 2021/04/08
 */
public class SecurityUtil {

    public static User getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            throw new InsufficientAuthenticationException(null);
        }
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            throw new InsufficientAuthenticationException(null);
        }
        return (User) authentication.getPrincipal();
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

}
