package com.itqingning.jaguar.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * Created by lvws on 2019/4/18.
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedisPassword redisPassword() {
        return RedisPassword.of(redisProperties.getPassword());
    }

    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration(RedisPassword redisPassword) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setDatabase(redisProperties.getDatabase());
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());
        configuration.setPassword(redisPassword);
        return configuration;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(RedisStandaloneConfiguration configuration) {
        return new JedisConnectionFactory(configuration);
    }

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
            JedisConnectionFactory jedisConnectionFactory,
            StringRedisSerializer keySerializer, GenericJackson2JsonRedisSerializer valueSerializer) {

        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        return redisTemplate;
    }
}
