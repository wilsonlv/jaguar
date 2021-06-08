package org.jaguar.commons.basecrud;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.extern.slf4j.Slf4j;
import org.jaguar.commons.web.exception.DataCrudException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lvws
 * @since 2019/5/6.
 */
@Slf4j
public abstract class BaseService<T extends BaseModel, M extends BaseMapper<T>> {

    public static final String DEFAULT_ORDER_COLUMN = "id_";

    @Autowired
    protected M mapper;


    @Transactional
    public void insert(T entity) {
        entity.setId(null);
        boolean success = SqlHelper.retBool(this.mapper.insert(entity));
        if (!success) {
            log.error("实体信息：" + entity.toString());
            throw new DataCrudException("数据插入失败！");
        }
    }

    @Transactional
    public void updateById(T entity) {
        boolean success = SqlHelper.retBool(this.mapper.updateById(entity));
        if (!success) {
            log.error("实体信息：" + entity.toString());
            throw new DataCrudException("数据更新失败！");
        }
    }

    @Transactional
    public void saveOrUpdate(T entity) {
        if (entity.getId() == null) {
            this.insert(entity);
        } else {
            this.updateById(entity);
        }
    }

    @Transactional
    public void delete(Long id) {
        boolean success = SqlHelper.retBool(this.mapper.deleteById(id));
        if (!success) {
            log.error("实体ID：" + id);
            throw new DataCrudException("数据删除失败！");
        }
    }

    @Transactional
    public void delete(T entity) {
        this.delete(Wrappers.lambdaUpdate(entity));
    }

    @Transactional
    public void delete(Wrapper<T> wrapper) {
        boolean success = SqlHelper.retBool(this.mapper.delete(wrapper));
        if (!success) {
            throw new DataCrudException("数据删除失败！");
        }
    }

    public T getById(Long id) {
        T t = this.mapper.selectById(id);
        Assert.validateId(t, id);
        return t;
    }

    public T unique(Wrapper<T> queryWrapper) {
        return this.unique(queryWrapper, true);
    }

    public T unique(Wrapper<T> queryWrapper, boolean throwEx) {
        if (throwEx) {
            return mapper.selectOne(queryWrapper);
        }

        List<T> list = mapper.selectList(queryWrapper);
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

    public Page<T> query(Page<T> page, Wrapper<T> queryWrapper) {
        if (page.orders().size() == 0) {
            page.orders().add(new OrderItem(DEFAULT_ORDER_COLUMN, false));
        }
        return mapper.selectPage(page, queryWrapper);
    }

    public List<T> list(Wrapper<T> queryWrapper) {
        return mapper.selectList(queryWrapper);
    }

    public int count(Wrapper<T> queryWrapper) {
        return SqlHelper.retCount(mapper.selectCount(queryWrapper));
    }

    public boolean exists(Wrapper<T> queryWrapper) {
        return this.count(queryWrapper) > 0;
    }

}
