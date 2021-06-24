package org.jaguar.commons.oauth2.util;

import org.jaguar.commons.oauth2.model.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
        if (authentication == null) {
            return null;
        }

        return (SecurityUser) authentication.getPrincipal();
    }

}
