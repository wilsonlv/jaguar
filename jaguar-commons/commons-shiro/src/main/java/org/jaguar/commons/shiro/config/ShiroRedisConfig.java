package org.jaguar.commons.shiro.config;

import io.lettuce.core.ReadFrom;
import org.apache.shiro.session.mgt.SimpleSession;
import org.jaguar.commons.shiro.RedisSessionDAO;
import org.jaguar.commons.shiro.ShiroRedisCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author lvws
 * @since 2019/11/6
 */
@Configuration
@ConditionalOnProperty("jaguar.shiro.redis.session-enable")
public class ShiroRedisConfig {

    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    private ShiroRedisProperties shiroRedisProperties;

    public int expire() {
        long seconds = serverProperties.getServlet().getSession().getTimeout().getSeconds();
        return (int) seconds;
    }

    @Bean
    public RedisTemplate<String, SimpleSession> redisManager(LettuceConnectionFactory lettuceConnectionFactory,
                                                             StringRedisSerializer keySerializer) {
        if (shiroRedisProperties.getReplicaEnable()) {
            LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
                    .readFrom(ReadFrom.REPLICA_PREFERRED).build();

            RedisStaticMasterReplicaConfiguration configuration = new RedisStaticMasterReplicaConfiguration(
                    shiroRedisProperties.getMasterHost(), shiroRedisProperties.getMasterPort());
            configuration.setPassword(shiroRedisProperties.getMasterPassword());
            for (String slave : shiroRedisProperties.getSlaves()) {
                String[] hostPort = slave.split(":");
                configuration.addNode(hostPort[0], Integer.parseInt(hostPort[1]));
            }

            lettuceConnectionFactory = new LettuceConnectionFactory(configuration, clientConfiguration);
        }

        RedisTemplate<String, SimpleSession> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(RedisSerializer.java(SimpleSession.class.getClassLoader()));
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisSessionDAO redisSessionDAO(RedisTemplate<String, SimpleSession> redisManager) {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager);
        redisSessionDAO.setExpire(expire());
        redisSessionDAO.setKeyPrefix(serverProperties.getServlet().getApplicationDisplayName() +
                ":" + redisSessionDAO.getKeyPrefix());
        return redisSessionDAO;
    }

    @Bean
    public ShiroRedisCacheManager shiroCacheManager(RedisTemplate<String, SimpleSession> redisManager) {
        ShiroRedisCacheManager shiroRedisCacheManager = new ShiroRedisCacheManager();
        shiroRedisCacheManager.setRedisManager(redisManager);
        shiroRedisCacheManager.setExpire(expire());
        shiroRedisCacheManager.setKeyPrefix(serverProperties.getServlet().getApplicationDisplayName()
                + ":" + shiroRedisCacheManager.getKeyPrefix());
        return shiroRedisCacheManager;
    }

}
