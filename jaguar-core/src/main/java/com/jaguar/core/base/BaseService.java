package com.jaguar.core.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jaguar.core.base.service.SysFieldEditLogService;
import com.jaguar.core.constant.Constant;
import com.jaguar.core.enums.OrderType;
import com.jaguar.core.util.ExecutorServiceUtil;
import com.jaguar.core.util.InstanceUtil;
import com.jaguar.redis.RedisCacheManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 业务逻辑层基类<br/>
 * 继承基类后必须配置CacheConfig(cacheNames="")
 *
 * @author lvws
 * @version 2016年5月20日 下午3:19:19
 */
public abstract class BaseService<T extends BaseModel, M extends BaseMapper<T>> {

    protected Logger logger = LogManager.getLogger(getClass());

    @Autowired
    protected M mapper;

    @Autowired
    protected SysFieldEditLogService sysFieldEditLogService;

    @Autowired
    protected RedisCacheManager cacheManager;

    /**
     * 构建分页对象
     */
    public static Page<Long> getPage(Map<String, Object> params) {
        Integer current = (Integer) params.get(Constant.PAGE);
        Integer size = (Integer) params.get(Constant.ROWS);
        if (current == null) {
            current = 1;
        }
        if (size == null) {
            size = 10;
        }
        if (size < 0) {
            return new Page<>();
        }

        Page<Long> page = new Page<>(current, size);

        String sort = (String) params.get(Constant.SORT);
        if (sort == null) {
            sort = "id_";
        }
        OrderType order = (OrderType) params.get(Constant.ORDER);
        if (order == null) {
            order = OrderType.DESC;
        }

        if (order == OrderType.ASC) {
            page.setAsc(sort);
        } else {
            page.setDesc(sort);
        }
        return page;
    }

    /**
     * 用ID page换取实体page
     */
    protected Page<T> getPage(Page<Long> ids) {
        if (ids != null) {
            Page<T> page = new Page<T>(ids.getCurrent(), ids.getSize());
            page.setTotal(ids.getTotal());
            List<T> records = InstanceUtil.newArrayList();
            for (Long id : ids.getRecords()) {
                records.add(this.getById(id));
            }
            page.setRecords(records);
            return page;
        }
        return new Page<T>();
    }

    /**
     * 用ID list换取实体list
     */
    public List<T> getList(List<Long> ids) {
        List<T> list = InstanceUtil.newArrayList();
        if (ids != null) {
            for (Long id : ids) {
                list.add(this.getById(id));
            }
        }
        return list;
    }

    /**
     * 根据ID查询实体
     */
    public T getById(Long id) {
        String key = getCacheKey(id);

        @SuppressWarnings("unchecked")
        T record = (T) cacheManager.get(key);
        if (record == null) {
            record = mapper.selectById(id);
            if (!TransactionSynchronizationManager.isSynchronizationActive()) {
                cacheManager.set(key, record);
            }
        }
        return record;
    }

    /**
     * 根据参数查询唯一
     */
    public T unique(Map<String, Object> params) {
        List<T> list = queryList(params);
        if (list.size() == 0) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        }

        throw new RuntimeException("expect one result to be returned, but found " + list.size());
    }

    /**
     * 根据参数分页查询
     */
    public Page<T> query(Map<String, Object> params) {
        Page<Long> page = getPage(params);
        page.setRecords(mapper.selectIdPage(page, params));
        return getPage(page);
    }

    /**
     * 根据参数查询列表
     */
    public List<T> queryList(Map<String, Object> params) {
        params.entrySet().removeIf(entry -> entry.getValue() == null);

        if (params.isEmpty()) {
            throw new RuntimeException("there must be one argument at least");
        }

        List<Long> ids = this.selectIdPage(params);
        return getList(ids);
    }

    /**
     * 插入或更新
     */
    @Transactional
    public T update(T record) {
        record.setUpdateTime(new Date());
        if (record.getId() == null) {
            record.setCreateTime(new Date());
            mapper.insert(record);
        } else {
            T org = getById(record.getId());
            T update = InstanceUtil.getDiff(org, record);
            update.setId(record.getId());
            mapper.updateById(update);

            final String cacheKey = getCacheKey(record.getId());
            cacheManager.del(cacheKey);

            if (TransactionSynchronizationManager.isSynchronizationActive()) {
                TransactionSynchronizationManager.registerSynchronization(
                        new TransactionSynchronizationAdapter() {
                            @Override
                            public void afterCommit() {
                                cacheManager.del(cacheKey);

                                saveEditLog(org, update);
                            }
                        }
                );
            } else {
                saveEditLog(org, update);
            }
        }
        record = mapper.selectById(record.getId());
        return record;
    }

    private <T extends BaseModel> void saveEditLog(final T org, final T update) {
        ExecutorServiceUtil.execute(() -> {
                    try {
                        sysFieldEditLogService.save(org, update);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    /**
     * 逻辑删除
     *
     * @param id     实体类Id
     * @param userId 操作人
     */
    @Transactional
    public Boolean del(final Long id, Long userId) {
        final T record = this.getById(id);
        record.setDeleted(true);
        record.setUpdateTime(new Date());
        record.setUpdateBy(userId);
        mapper.updateById(record);

        final String cacheKey = getCacheKey(record.getId());
        cacheManager.del(cacheKey);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            cacheManager.del(cacheKey);
                        }
                    }
            );
        }
        return true;
    }

    /**
     * 物理删除，慎用
     */
    @Transactional
    public Boolean delete(final Long id) {
        mapper.delete(id);

        final String cacheKey = getCacheKey(id);
        cacheManager.del(cacheKey);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            cacheManager.del(cacheKey);
                        }
                    }
            );
        }
        return true;
    }

    /**
     * 数据库直接查询
     */
    public T selectById(Long id) {
        return this.mapper.selectById(id);
    }

    /**
     * 数据库直接查询
     */
    public List<T> selectList(Wrapper<T> wrapper) {
        return this.mapper.selectList(wrapper);
    }

    /**
     * 数据库直接查询
     */
    public IPage<T> selectPage(IPage<T> page, Wrapper<T> wrapper) {
        return this.mapper.selectPage(page, wrapper);
    }

    /**
     * 查询ID集合
     */
    public List<Long> selectIdPage(Map<String, Object> params) {
        return mapper.selectIdPage(params);
    }

    /**
     * 根据参数查询结果集长度
     */
    public Integer size(Map<String, Object> params) {
        List<Long> ids = this.selectIdPage(params);
        return ids.size();
    }

    /**
     * 根据参数查询结果集是否存在
     */
    public Boolean exists(Map<String, Object> params) {
        return this.size(params) > 0;
    }

    /**
     * 获取缓存键值
     */
    protected String getCacheKey(Object id) {
        String cacheName;
        CacheConfig cacheConfig = getClass().getAnnotation(CacheConfig.class);
        if (cacheConfig == null || cacheConfig.cacheNames().length == 0) {
            cacheName = getClass().getName();
        } else {
            cacheName = cacheConfig.cacheNames()[0];
        }
        return new StringBuilder(cacheManager.getNamespace()).append(":").append(cacheName).append(":").append(id).toString();
    }
}
