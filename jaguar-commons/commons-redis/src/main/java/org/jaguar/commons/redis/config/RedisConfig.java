package org.jaguar.commons.redis.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2019/4/18.
 */
@Configuration
public class RedisConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Bean("keySerializer")
    public StringRedisSerializer keySerializer() {
        return new StringRedisSerializer();
    }

    @Bean("valueSerializer")
    public GenericJackson2JsonRedisSerializer valueSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisProperties.Pool pool = redisProperties.getJedis().getPool();

        RedisStandaloneConfiguration configuration =new RedisStandaloneConfiguration();
        configuration.setDatabase(redisProperties.getDatabase());
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());
        configuration.setPassword(redisProperties.getPassword());

        JedisConnectionFactory factory = new JedisConnectionFactory(configuration);
        GenericObjectPoolConfig poolConfig = factory.getPoolConfig();
        poolConfig.setMaxIdle(pool.getMaxIdle());
        poolConfig.setMaxTotal(pool.getMaxActive());
        poolConfig.setMinIdle(pool.getMinIdle());
        return factory;
    }

    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(
            JedisConnectionFactory jedisConnectionFactory,
            StringRedisSerializer keySerializer, GenericJackson2JsonRedisSerializer valueSerializer) {

        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        return redisTemplate;
    }
}
