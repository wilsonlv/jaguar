package top.wilsonlv.jaguar.basecrud;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;
import top.wilsonlv.jaguar.commons.web.exception.impl.DataCrudException;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author lvws
 * @since 2019/5/6.
 */
public abstract class BaseService<E extends BaseModel, M extends BaseMapper<E>> {

    public static final String DEFAULT_ORDER_COLUMN = "id_";

    public static final String LIMIT_1 = "limit 1";

    public static final int DEFAULT_BATCH_SIZE = 1000;

    @Autowired
    protected M mapper;

    @Resource
    protected BatchOperation<E, M> batchOperation;

    protected final Log log = LogFactory.getLog(this.getClass());

    protected final Class<E> entityClass = this.currentModelClass();

    protected final Class<M> mapperClass = this.currentMapperClass();


    protected Class<E> currentModelClass() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) parameterizedType.getActualTypeArguments()[0];
        return clazz;
    }

    protected Class<M> currentMapperClass() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        @SuppressWarnings("unchecked") Class<M> clazz = (Class<M>) parameterizedType.getActualTypeArguments()[1];
        return clazz;
    }


    @Transactional
    public void insert(E entity) {
        entity.setId(null);
        boolean success = SqlHelper.retBool(this.mapper.insert(entity));
        if (!success) {
            log.error("??????????????????: {}" + entity.toString());
            throw new DataCrudException("?????????????????????");
        }
    }

    @Transactional
    public void batchInsert(Collection<E> entityList) {
        this.batchInsert(entityList, DEFAULT_BATCH_SIZE);
    }

    @Transactional
    public void batchInsert(Collection<E> entityList, int batchSize) {
        batchOperation.batchInsertById(this.entityClass, this.mapperClass, log, entityList, batchSize);
    }

    @Transactional
    public void updateById(E entity) {
        boolean success = SqlHelper.retBool(this.mapper.updateById(entity));
        if (!success) {
            log.error("?????????????????????" + entity.toString());
            throw new DataCrudException("?????????????????????");
        }
    }

    @Transactional
    public void batchUpdateById(Collection<E> entityList) {
        this.batchUpdateById(entityList, DEFAULT_BATCH_SIZE);
    }

    /**
     * ??????????????????set?????????????????????
     *
     * @param entityList ????????????
     * @param batchSize  ???????????????
     */
    @Transactional
    public void batchUpdateById(Collection<E> entityList, int batchSize) {
        batchOperation.batchUpdateById(this.entityClass, this.mapperClass, log, entityList, batchSize);
    }

    @Transactional
    public void saveOrUpdate(E entity) {
        if (entity.getId() == null) {
            this.insert(entity);
        } else {
            this.updateById(entity);
        }
    }

    @Transactional
    public void delete(Long id) {
        E t;
        try {
            t = this.entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CheckedException(e);
        }
        t.setId(id);
        t.setUpdateTime(LocalDateTime.now());
        this.updateById(t);

        this.mapper.deleteById(id);
    }

    @Transactional
    public void delete(E entity) {
        this.delete(Wrappers.lambdaQuery(entity));
    }

    @Transactional
    public Collection<E> delete(LambdaQueryWrapper<E> wrapper) {
        List<E> entityIds = this.list(wrapper.select(E::getId));
        if (entityIds.size() == 0) {
            return Collections.emptySet();
        }

        LocalDateTime now = LocalDateTime.now();
        Set<Long> ids = new HashSet<>(entityIds.size());
        for (E e : entityIds) {
            e.setUpdateTime(now);
            ids.add(e.getId());
        }
        this.batchUpdateById(entityIds);

        this.mapper.deleteBatchIds(ids);

        return entityIds;
    }

    public E getById(Long id) {
        return this.getById(id, true);
    }

    public E getById(Long id, boolean thorwEx) {
        E e = this.mapper.selectById(id);
        if (thorwEx) {
            Assert.validateId(e, "??????", id);
        }
        return e;
    }

    public E unique(Wrapper<E> queryWrapper) {
        return this.unique(queryWrapper, true);
    }

    public E unique(Wrapper<E> queryWrapper, boolean throwEx) {
        if (throwEx) {
            return mapper.selectOne(queryWrapper);
        }

        List<E> list = mapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            int size = list.size();
            if (size > 1) {
                log.warn(String.format("Warn: execute Method There are  %s results.", size));
            }
            return list.get(0);
        } else {
            return null;
        }
    }

    public Page<E> query(Page<E> page, Wrapper<E> queryWrapper) {
        if (page.orders().size() == 0) {
            page.orders().add(new OrderItem(DEFAULT_ORDER_COLUMN, false));
        }
        return mapper.selectPage(page, queryWrapper);
    }

    public List<E> list(Wrapper<E> queryWrapper) {
        return mapper.selectList(queryWrapper);
    }

    public int count(Wrapper<E> queryWrapper) {
        return SqlHelper.retCount(mapper.selectCount(queryWrapper));
    }

    public boolean exists(Wrapper<E> queryWrapper) {
        return this.count(queryWrapper) > 0;
    }

    public <T> void afterTransactionCommit(Consumer<T> consumer, T params) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            consumer.accept(params);
                        }
                    }
            );
        }
    }

    public <V> Page<V> toVoPage(Page<E> page) {
        Page<V> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(new ArrayList<>(page.getRecords().size()));
        voPage.setOrders(page.getOrders());
        voPage.setCountId(page.getCountId());
        page.setMaxLimit(page.getMaxLimit());
        return voPage;
    }

}
