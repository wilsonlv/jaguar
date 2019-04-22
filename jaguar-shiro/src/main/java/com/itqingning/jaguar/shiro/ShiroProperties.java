package com.itqingning.jaguar.shiro;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lvws on 2019/4/22.
 */
@Configuration
@ConfigurationProperties(prefix = "shiro")
public class ShiroProperties {

    private String cookieName = "JAGUAR-JSESSIONID";

    /**
     * 会话过期时间（毫秒）
     */
    private Long sessionTimeoutMillis = 1800000L;

    private String loginUrl = "/unauthorized";

    private String unauthorizedUrl = "/forbidden";

    private String filterChainDefinitions = "/=anon\n" +
            "/index.jsp=anon\n" +
            "/*.ico=anon\n" +
            "/unauthorized=anon\n" +
            "/forbidden=anon\n" +
            "/swagger**=anon\n" +
            "/swagger-resources/**=anon\n" +
            "/webjars/**=anon\n" +
            "/*/api-docs=anon\n" +
            "/login=anon\n" +
            "/pic_verification_code=anon\n" +
            "/test=anon\n" +
            "/test/**=anon\n" +
            "/**=authc";

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public Long getSessionTimeoutMillis() {
        return sessionTimeoutMillis;
    }

    public void setSessionTimeoutMillis(Long sessionTimeoutMillis) {
        this.sessionTimeoutMillis = sessionTimeoutMillis;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public String getFilterChainDefinitions() {
        return filterChainDefinitions;
    }

    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }
}
