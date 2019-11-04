package org.jaguar.commons.shiro.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.jaguar.commons.redis.config.RedisProperties;
import org.jaguar.commons.shiro.listener.SessionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lvws on 2019/4/30.
 */
@Configuration
@AutoConfigureAfter(ShiroProperties.class)
public class ShiroSessionConfig {

    @Autowired
    private RedisProperties redisProperties;
    @Autowired
    private ServerProperties serverProperties;

    public int expire() {
        Long seconds = serverProperties.getServlet().getSession().getTimeout().getSeconds();
        return seconds.intValue();
    }

    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisProperties.getHost() + ":" + redisProperties.getPort());
        redisManager.setDatabase(redisProperties.getDatabase());
        if (StringUtils.isNotBlank(redisProperties.getPassword())) {
            redisManager.setPassword(redisProperties.getPassword());
        }
        return redisManager;
    }

    @Bean
    public RedisSessionDAO redisSessionDAO(RedisManager redisManager) {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setKeyPrefix(redisProperties.getNamespace() + ":" + redisSessionDAO.getKeyPrefix());
        redisSessionDAO.setRedisManager(redisManager);
        redisSessionDAO.setExpire(expire());
        return redisSessionDAO;
    }

    @Bean
    public RedisCacheManager shiroCacheMananger(RedisManager redisManager) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        redisCacheManager.setExpire(expire());
        redisCacheManager.setKeyPrefix(redisProperties.getNamespace() + ":" + redisCacheManager.getKeyPrefix());
        return redisCacheManager;
    }

    @Bean
    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO, @Value("${shiro.cookie-name}") String cookieName) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(expire() * 1000);
        sessionManager.setSessionDAO(redisSessionDAO);
        sessionManager.setSessionIdCookie(new SimpleCookie(cookieName));
        sessionManager.getSessionListeners().add(new SessionListener());
        return sessionManager;
    }
}
