package org.jaguar.support.redis.cache;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jaguar.core.base.BaseModel;
import org.jaguar.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import static org.jaguar.support.redis.cache.EntityCacheableStrategy.ALWAYS;
import static org.jaguar.support.redis.cache.EntityCacheableStrategy.NON_TRANSACTIONAL;

/**
 * @author lvws
 * @since 2020/9/16
 */
public abstract class AbstractRedisCacheService<T extends BaseModel, M extends BaseMapper<T>> extends BaseService<T, M> {

    @Autowired
    protected RedisTemplate<String, Serializable> redisTemplate;

    protected EntityCacheableStrategy getEntityCacheableStrategy() {
        return NON_TRANSACTIONAL;
    }

    /**
     * 直接把结果缓存
     *
     * @param id 实体ID
     * @return 实体
     */
    public T getCache(Long id) {
        String cacheKey = this.getCacheKey(id);
        BoundValueOperations<String, Serializable> operations = redisTemplate.boundValueOps(cacheKey);

        @SuppressWarnings("unchecked")
        T cacheEntity = (T) operations.get();
        if (cacheEntity == null) {
            cacheEntity = super.getById(id);
            operations.set(cacheEntity, 30, TimeUnit.DAYS);
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

            switch (this.getEntityCacheableStrategy()) {
                case NON_TRANSACTIONAL: {
                    if (TransactionSynchronizationManager.isSynchronizationActive()) {
                        break;
                    }
                }
                case ALWAYS: {
                    operations.set(cacheEntity, 30, TimeUnit.DAYS);
                }
                default:
            }
        }
        return cacheEntity;
    }

    @Override
    @Transactional
    public T insert(T entity) {
        entity = super.insert(entity);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            String cacheKey = this.getCacheKey(entity.getId());

            EntityCacheableStrategy strategy = this.getEntityCacheableStrategy();
            T finalEntity = entity;

            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            if (strategy == ALWAYS) {
                                redisTemplate.boundValueOps(cacheKey).set(finalEntity, 30, TimeUnit.DAYS);
                            }
                        }
                    }
            );
        }
        return entity;
    }

    @Override
    @Transactional
    public T updateById(T entity) {
        String cacheKey = this.getCacheKey(entity.getId());
        redisTemplate.delete(cacheKey);

        entity = super.updateById(entity);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            EntityCacheableStrategy strategy = this.getEntityCacheableStrategy();
            T finalEntity = entity;

            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            switch (strategy) {
                                case NON_TRANSACTIONAL: {
                                    redisTemplate.delete(cacheKey);
                                    break;
                                }
                                case ALWAYS: {
                                    redisTemplate.boundValueOps(cacheKey).set(finalEntity, 30, TimeUnit.DAYS);
                                }
                                default:
                            }
                        }
                    }
            );
        }
        return entity;
    }

    @Override
    @Transactional
    public void delete(T entity) {
        String cacheKey = this.getCacheKey(entity.getId());
        redisTemplate.delete(cacheKey);
        super.delete(entity);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            redisTemplate.delete(cacheKey);
                        }
                    }
            );
        }
    }

    /**
     * 获取缓存键值
     */
    protected String getCacheKey(Long id) {
        ParameterizedType p = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type model = p.getActualTypeArguments()[0];
        return model.getTypeName() + ":" + id;
    }
}
