package top.wilsonlv.jaguar.commons.basecrud;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;
import top.wilsonlv.jaguar.commons.web.exception.impl.DataCrudException;

import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author lvws
 * @since 2019/5/6.
 */
@Slf4j
public abstract class BaseService<E extends BaseModel, M extends BaseMapper<E>> {

    public static final String DEFAULT_ORDER_COLUMN = "id_";

    public static final String LIMIT_1 = "limit 1";

    @Autowired
    protected M mapper;

    @Transactional
    public void insert(E entity) {
        entity.setId(null);
        boolean success = SqlHelper.retBool(this.mapper.insert(entity));
        if (!success) {
            log.error("实体信息：" + entity.toString());
            throw new DataCrudException("数据插入失败！");
        }
    }

    @Transactional
    public void updateById(E entity) {
        boolean success = SqlHelper.retBool(this.mapper.updateById(entity));
        if (!success) {
            log.error("实体信息：" + entity.toString());
            throw new DataCrudException("数据更新失败！");
        }
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
        E e = this.getById(id);
        e.setUpdateTime(LocalDateTime.now());
        e.setDeleted(true);
        boolean success = SqlHelper.retBool(this.mapper.updateById(e));
        if (!success) {
            log.error("实体ID：" + id);
            throw new DataCrudException("数据删除失败！");
        }
    }

    @Transactional
    public void delete(E entity) {
        this.delete(Wrappers.lambdaUpdate(entity));
    }

    @Transactional
    public void delete(Wrapper<E> wrapper) {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) parameterizedType.getActualTypeArguments()[0];
        E t;
        try {
            t = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CheckedException(e);
        }
        t.setUpdateTime(LocalDateTime.now());
        t.setDeleted(true);
        this.mapper.update(t, wrapper);
    }

    public E getById(Long id) {
        return this.getById(id, true);
    }

    public E getById(Long id, boolean thorwEx) {
        E e = this.mapper.selectById(id);
        if (thorwEx) {
            Assert.validateId(e, "实体", id);
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

}
