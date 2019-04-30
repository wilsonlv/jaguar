package com.jaguar.shiro;

import com.jaguar.redis.RedisProperties;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lvws on 2019/4/30.
 */
@Configuration
public class ShiroSessionConfig {

    @Autowired
    private RedisProperties redisProperties;
    @Autowired
    private ShiroProperties shiroProperties;
    @Autowired
    private ServerProperties serverProperties;

    public int expire() {
        Long seconds = serverProperties.getServlet().getSession().getTimeout().getSeconds();
        return seconds.intValue();
    }

    @Bean
    public SessionListener sessionListener() {
        return new SessionListener();
    }

    @Bean
    public SimpleCookie simpleCookie() {
        return new SimpleCookie(shiroProperties.getCookieName());
    }

    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisProperties.getHost() + ":" + redisProperties.getPort());
        redisManager.setDatabase(redisProperties.getDatabase());
        redisManager.setPassword(redisManager.getPassword());
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
    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO, SimpleCookie simpleCookie,
                                         SessionListener sessionListener) {

        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(expire() * 1000);
        sessionManager.setSessionDAO(redisSessionDAO);
        sessionManager.setSessionIdCookie(simpleCookie);
        sessionManager.getSessionListeners().add(sessionListener);
        return sessionManager;
    }

    @Bean("shiroCacheMananger")
    public RedisCacheManager redisCacheManager(RedisManager redisManager) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        redisCacheManager.setExpire(expire());
        redisCacheManager.setKeyPrefix(redisProperties.getNamespace() + ":" + redisCacheManager.getKeyPrefix());
        return redisCacheManager;
    }

}
