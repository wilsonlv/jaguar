package org.jaguar.commons.shiro.config;

import org.apache.commons.lang3.StringUtils;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lvws
 * @since 2019/11/6
 */
@Configuration
@ConditionalOnProperty("jaguar.shiro.redis-session-enable")
public class ShiroRedisConfig {

    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    private RedisProperties redisProperties;

    public int expire() {
        long seconds = serverProperties.getServlet().getSession().getTimeout().getSeconds();
        return (int) seconds;
    }

    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisProperties.getHost() + ":" + redisProperties.getHost());
        redisManager.setDatabase(redisProperties.getDatabase());
        if (StringUtils.isNotBlank(redisProperties.getPassword())) {
            redisManager.setPassword(redisProperties.getPassword());
        }
        return redisManager;
    }

    @Bean
    public RedisSessionDAO redisSessionDAO(RedisManager redisManager) {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager);
        redisSessionDAO.setExpire(expire());
        redisSessionDAO.setKeyPrefix(serverProperties.getServlet().getApplicationDisplayName() +
                ":" + redisSessionDAO.getKeyPrefix());
        return redisSessionDAO;
    }

    @Bean
    public RedisCacheManager shiroCacheManager(RedisManager redisManager) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        redisCacheManager.setExpire(expire());
        redisCacheManager.setKeyPrefix(serverProperties.getServlet().getApplicationDisplayName()
                + ":" + redisCacheManager.getKeyPrefix());
        return redisCacheManager;
    }

}
