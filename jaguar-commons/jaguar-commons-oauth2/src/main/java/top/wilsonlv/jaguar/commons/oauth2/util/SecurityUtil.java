package top.wilsonlv.jaguar.commons.oauth2.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityUser;

/**
 * @author lvws
 * @since 2021/6/24
 */
public final class SecurityUtil {

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static SecurityUser getCurrentUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof SecurityUser) {
            return (SecurityUser) principal;
        }
        return null;
    }

}
