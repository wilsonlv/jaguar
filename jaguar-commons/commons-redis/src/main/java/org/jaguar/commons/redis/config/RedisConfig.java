package org.jaguar.commons.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * @author lvws
 * @since  2019/4/18.
 */
@Configuration
public class RedisConfig {

    @Bean("keySerializer")
    public StringRedisSerializer keySerializer() {
        return new StringRedisSerializer();
    }

    @Bean("valueSerializer")
    public GenericJackson2JsonRedisSerializer valueSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(
            RedisConnectionFactory redisConnectionFactory,
            StringRedisSerializer keySerializer, GenericJackson2JsonRedisSerializer valueSerializer) {

        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        return redisTemplate;
    }
}
