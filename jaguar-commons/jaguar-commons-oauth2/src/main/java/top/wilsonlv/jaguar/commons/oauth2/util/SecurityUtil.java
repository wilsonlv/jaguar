package top.wilsonlv.jaguar.commons.oauth2.util;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityUser;
import top.wilsonlv.jaguar.commons.web.util.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @author lvws
 * @since 2021/6/24
 */
@Slf4j
public final class SecurityUtil {

    private static final String BASIC_ = "Basic ";

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

    public static String getClientId() {
        HttpServletRequest request = WebUtil.getRequest();
        if (request == null) {
            return null;
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        return extractClientId(header);
    }

    /**
     * 解析clientId
     *
     * @param header 请求头
     * @return clientId
     */
    public static String extractClientId(String header) {
        if (StringUtils.isBlank(header) || !header.startsWith(BASIC_)) {
            log.debug("请求头中client信息为空: {}", header);
            return null;
        }

        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            log.debug("Failed to decode basic authentication token: {}", header);
            return null;
        }

        String token = new String(decoded, StandardCharsets.UTF_8);

        int colon = token.indexOf(":");
        if (colon == -1) {
            log.debug("Invalid basic authentication token: {}", header);
            return null;
        }
        return token.substring(0, colon);
    }

}
