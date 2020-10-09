package org.jaguar.commons.shiro.config;

import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.jaguar.commons.shiro.listener.SessionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lvws
 * @since 2019/4/30.
 */
@Configuration
public class ShiroSessionConfig {

    @Autowired
    private ServerProperties serverProperties;

    private int expire() {
        long seconds = serverProperties.getServlet().getSession().getTimeout().getSeconds();
        return (int) seconds;
    }

    @Bean
    public SessionManager sessionManager(@Value("${jaguar.shiro.cookie-name}") String cookieName,
                                         @Autowired(required = false) SessionDAO sessionDAO) {

        SimpleCookie simpleCookie = new SimpleCookie(cookieName);
        simpleCookie.setMaxAge(60 * 60 * 24 * 365);

        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(expire() * 1000);
        sessionManager.setSessionIdCookie(simpleCookie);
        sessionManager.getSessionListeners().add(new SessionListener());
        if (sessionDAO != null) {
            sessionManager.setSessionDAO(sessionDAO);
        }
        return sessionManager;
    }

}
