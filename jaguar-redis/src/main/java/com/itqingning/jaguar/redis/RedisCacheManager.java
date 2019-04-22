package com.itqingning.jaguar.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by apple on 2017/2/22.
 */
@Component
public class RedisCacheManager {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    private RedisProperties redisProperties;

    public Integer getExpiration() {
        return redisProperties.getExpiration();
    }

    public String getNamespace() {
        return redisProperties.getNamespace();
    }

    /**
     * 模糊匹配
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 根据key获取vlaue，并刷新缓存的过期时间
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return get(key, true);
    }

    /**
     * 根据key获取vlaue
     *
     * @param key
     * @param fresh 是否刷新缓存的过期时间
     *              true：是
     *              flase：否
     * @return
     */
    public Object get(String key, boolean fresh) {
        BoundValueOperations<String, Serializable> operations = redisTemplate.boundValueOps(key);
        if (fresh) {
            operations.expire(getExpiration(), TimeUnit.SECONDS);
        }
        return operations.get();
    }

    /**
     * 设置key-value，默认有效期
     *
     * @param key
     * @param value
     */
    public void set(String key, Serializable value) {
        set(key, value, getExpiration());
    }

    /**
     * 设置key-value和有效期，-1为永久
     *
     * @param key
     * @param value
     * @param seconds
     */
    public void set(String key, Serializable value, int seconds) {
        if (seconds == -1) {
            redisTemplate.boundValueOps(key).set(value);
        } else {
            redisTemplate.boundValueOps(key).set(value, seconds, TimeUnit.SECONDS);
        }
    }

    /**
     * 目标key是否存在
     *
     * @param key
     * @return
     */
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除目标key缓存
     *
     * @param key
     */
    public void del(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 目标key在几秒后是否过期
     *
     * @param key
     * @param seconds
     * @return
     */
    public Boolean expire(String key, int seconds) {
        return redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    /**
     * 目标key在某时刻是否过期
     *
     * @param key
     * @param unixTime
     * @return
     */
    public Boolean expireAt(String key, long unixTime) {
        return redisTemplate.expireAt(key, new Date(unixTime));
    }

    /**
     * 从左侧压入队列
     */
    public long leftPush(String key, Serializable value) {
        return redisTemplate.boundListOps(key).leftPush(value);
    }

    /**
     * 从右侧压入队列
     */
    public long rightPush(String key, Serializable value) {
        return redisTemplate.boundListOps(key).rightPush(value);
    }

    /**
     * 从左侧检索
     */
    public List<Serializable> lrange(String key, long start, long end) {
        return redisTemplate.boundListOps(key).range(start, end);
    }

    /**
     * 从作侧压出队列
     */
    public Serializable leftPop(String key) {
        return redisTemplate.boundListOps(key).leftPop();
    }

    /**
     * 从右侧压出队列
     */
    public Serializable rightPop(String key) {
        return redisTemplate.boundListOps(key).rightPop();
    }

    /**
     * 队列长度
     */
    public Long lsize(String key) {
        return redisTemplate.boundListOps(key).size();
    }

}
