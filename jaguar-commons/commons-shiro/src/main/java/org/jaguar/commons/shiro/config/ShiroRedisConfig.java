package org.jaguar.commons.shiro.config;

import org.apache.commons.lang3.StringUtils;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.jaguar.commons.redis.config.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
    private RedisProperties redisProperties;
    @Autowired
    private ServerProperties serverProperties;

    public int expire() {
        long seconds = serverProperties.getServlet().getSession().getTimeout().getSeconds();
        return (int) seconds;
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
    public RedisCacheManager shiroCacheManager(RedisManager redisManager) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        redisCacheManager.setExpire(expire());
        redisCacheManager.setKeyPrefix(redisProperties.getNamespace() + ":" + redisCacheManager.getKeyPrefix());
        return redisCacheManager;
    }

}
