package org.jaguar.support.rediscache;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.extern.slf4j.Slf4j;
import org.jaguar.commons.basecrud.Assert;
import org.jaguar.commons.basecrud.BaseMapper;
import org.jaguar.commons.basecrud.BaseModel;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.commons.oauth2.model.SecurityUser;
import org.jaguar.commons.oauth2.util.SecurityUtil;
import org.jaguar.commons.web.exception.impl.CheckedException;
import org.jaguar.commons.web.exception.impl.DataCrudException;
import org.jaguar.support.fieldeditlog.model.FieldEditLoggable;
import org.jaguar.support.fieldeditlog.service.FieldEditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author lvws
 * @since 2020/9/16
 */
@Slf4j
public abstract class AbstractRedisCacheService<T extends BaseModel, M extends BaseMapper<T>> extends BaseService<T, M> {

    @Autowired(required = false)
    private FieldEditLogService fieldEditLogService;
    @Autowired
    protected RedisTemplate<String, Serializable> redisTemplate;

    /**
     * TimeUnit.DAYS
     */
    public int getTimeout() {
        return 15;
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

            }
        }
        return cacheEntity;
    }


    @Override
    @Transactional
    public void updateById(T entity) {
        String cacheKey = this.getCacheKey(entity.getId());
        redisTemplate.delete(cacheKey);

        T org = this.mapper.selectById(entity.getId());
        Assert.validateId(org, "实体", entity.getId());

        if (entity instanceof FieldEditLoggable && this.fieldEditLogService != null) {
            SecurityUser currentUser = SecurityUtil.getCurrentUser();
            FieldEditLoggable fieldEditLoggable = (FieldEditLoggable) entity;
            fieldEditLoggable.setUpdateBy(currentUser.getUsername());
            fieldEditLoggable.setUpdateTime(LocalDateTime.now());

            try {
                this.fieldEditLogService.logEdit((FieldEditLoggable) org, fieldEditLoggable);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
                throw new CheckedException(e.getMessage());
            }
        }

        boolean success = SqlHelper.retBool(this.mapper.updateById(entity));
        if (!success) {
            log.error("实体信息：" + entity.toString());
            throw new DataCrudException("数据更新失败！");
        } else {
            entity = this.mapper.selectById(entity.getId());
        }

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
