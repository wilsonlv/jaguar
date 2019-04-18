package com.itqingning.jaguar.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("classpath:redis.properties")
public class RedisConfig {

    @Value("${spring.redis.database}")
    private int database;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedisPassword redisPassword() {
        return RedisPassword.of(password);
    }

    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration(RedisPassword redisPassword) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setDatabase(database);
        configuration.setHostName(host);
        configuration.setPort(port);
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
