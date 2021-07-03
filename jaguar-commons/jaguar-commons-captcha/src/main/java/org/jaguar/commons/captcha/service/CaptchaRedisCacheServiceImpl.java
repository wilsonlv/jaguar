package org.jaguar.commons.captcha.service;

import com.anji.captcha.properties.AjCaptchaProperties;
import com.anji.captcha.service.CaptchaCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author lvws
 * @since 2021/7/2
 */
@Component
public class CaptchaRedisCacheServiceImpl implements CaptchaCacheService {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public void set(String key, String value, long expiresInSeconds) {
        redisTemplate.boundValueOps(key).set(value, expiresInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public boolean exists(String key) {
        Boolean exists = redisTemplate.hasKey(key);
        return exists != null ? exists : false;
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public String get(String key) {
        return (String) redisTemplate.boundValueOps(key).get();
    }

    @Override
    public String type() {
        return AjCaptchaProperties.StorageType.redis.name();
    }

}
