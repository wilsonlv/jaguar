package top.wilsonlv.jaguar.support.rediscachelog.service;

import lombok.extern.slf4j.Slf4j;
import top.wilsonlv.jaguar.commons.basecrud.BaseMapper;
import top.wilsonlv.jaguar.commons.rediscache.properties.RedisCacheProperties;
import top.wilsonlv.jaguar.support.fieldeditlog.model.FieldEditLoggable;
import top.wilsonlv.jaguar.support.fieldeditlog.service.AbstractFieldEditLoggableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

/**
 * @author lvws
 * @since 2020/9/16
 */
@Slf4j
public abstract class AbstractRedisCacheFieldEditLoggableService<T extends FieldEditLoggable, M extends BaseMapper<T>> extends AbstractFieldEditLoggableService<T, M> {

    @Autowired
    protected RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    private RedisCacheProperties redisCacheProperties;

    /**
     * TimeUnit.DAYS
     */
    public long getTimeout() {
        return redisCacheProperties.getExpire();
    }

    /**
     * 获取缓存键值
     */
    protected String getCacheKey(Long id) {
        ParameterizedType p = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type model = p.getActualTypeArguments()[0];
        return model.getTypeName() + ":" + id;
    }


    public T getCache(Long id) {
        String cacheKey = this.getCacheKey(id);
        BoundValueOperations<String, Serializable> operations = redisTemplate.boundValueOps(cacheKey);

        @SuppressWarnings("unchecked")
        T cacheEntity = (T) operations.get();
        if (cacheEntity == null) {
            cacheEntity = super.getById(id);
            operations.set(cacheEntity, getTimeout(), TimeUnit.DAYS);
        }
        return cacheEntity;
    }

    @Override
    public T getById(Long id) {
        String cacheKey = this.getCacheKey(id);
        BoundValueOperations<String, Serializable> operations = redisTemplate.boundValueOps(cacheKey);

        @SuppressWarnings("unchecked")
        T cacheEntity = (T) operations.get();
        if (cacheEntity == null) {
            cacheEntity = super.getById(id);

            if (!TransactionSynchronizationManager.isSynchronizationActive()) {
                operations.set(cacheEntity, getTimeout(), TimeUnit.DAYS);
            }
        }
        return cacheEntity;
    }


    @Override
    @Transactional
    public void updateById(T entity) {
        String cacheKey = this.getCacheKey(entity.getId());
        redisTemplate.delete(cacheKey);

        super.updateById(entity);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            redisTemplate.delete(cacheKey);
                        }
                    }
            );
        }
    }

    @Override
    @Transactional
    public void delete(T entity) {
        String cacheKey = this.getCacheKey(entity.getId());
        redisTemplate.delete(cacheKey);

        super.delete(entity);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            redisTemplate.delete(cacheKey);
                        }
                    }
            );
        }
    }

}
