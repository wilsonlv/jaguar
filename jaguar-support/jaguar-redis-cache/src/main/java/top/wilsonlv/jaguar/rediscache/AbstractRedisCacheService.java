package top.wilsonlv.jaguar.rediscache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;
import top.wilsonlv.jaguar.basecrud.BaseMapper;
import top.wilsonlv.jaguar.basecrud.BaseModel;
import top.wilsonlv.jaguar.basecrud.BaseService;
import top.wilsonlv.jaguar.rediscache.properties.RedisCacheProperties;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author lvws
 * @since 2020/9/16
 */
@Slf4j
public abstract class AbstractRedisCacheService<E extends BaseModel, M extends BaseMapper<E>> extends BaseService<E, M> {

    @Resource
    protected RedisTemplate<String, Serializable> redisTemplate;

    @Resource
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

    protected void batchDeleteCache(Collection<E> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }

        Set<String> keys = new HashSet<>(entities.size());
        for (E e : entities) {
            keys.add(getCacheKey(e.getId()));
        }
        redisTemplate.delete(keys);
    }


    public E getCache(Long id) {
        String cacheKey = this.getCacheKey(id);
        BoundValueOperations<String, Serializable> operations = redisTemplate.boundValueOps(cacheKey);

        @SuppressWarnings("unchecked")
        E cacheEntity = (E) operations.get();
        if (cacheEntity == null) {
            cacheEntity = super.getById(id);
            operations.set(cacheEntity, getTimeout(), TimeUnit.DAYS);
        }
        return cacheEntity;
    }

    @Override
    public E getById(Long id) {
        String cacheKey = this.getCacheKey(id);
        BoundValueOperations<String, Serializable> operations = redisTemplate.boundValueOps(cacheKey);

        @SuppressWarnings("unchecked")
        E cacheEntity = (E) operations.get();
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
    public void updateById(E entity) {
        String cacheKey = this.getCacheKey(entity.getId());
        redisTemplate.delete(cacheKey);

        super.updateById(entity);

        afterTransactionCommit((key) -> redisTemplate.delete(key), cacheKey);
    }

    @Override
    @Transactional
    public void batchUpdateById(Collection<E> entityList, int batchSize) {
        super.batchUpdateById(entityList, batchSize);
        afterTransactionCommit(this::batchDeleteCache, entityList);
    }

    @Override
    public void delete(Long id) {
        String cacheKey = this.getCacheKey(id);
        redisTemplate.delete(cacheKey);

        super.delete(id);

        afterTransactionCommit((key) -> redisTemplate.delete(key), cacheKey);
    }

    @Override
    public Collection<E> delete(LambdaQueryWrapper<E> wrapper) {
        Collection<E> entities = super.delete(wrapper);
        afterTransactionCommit(this::batchDeleteCache, entities);
        return entities;
    }


}
