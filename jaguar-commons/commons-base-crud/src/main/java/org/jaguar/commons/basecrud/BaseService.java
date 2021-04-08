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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author lvws
 * @since 2019/5/6.
 */
@Slf4j
public abstract class BaseService<T extends BaseModel, M extends BaseMapper<T>> {

    @Autowired
    protected M mapper;

    public static final String DEFAULT_ORDER_COLUMN = "id_";


    @Transactional
    public T insert(T entity) {
        entity.setId(null);
        entity.setCreateTime(LocalDateTime.now());

        boolean success = SqlHelper.retBool(this.mapper.insert(entity));
        if (!success) {
            log.error("实体信息：" + entity.toString());
            throw new DataCrudException("数据插入失败！");
        }

        return this.getById(entity.getId());
    }

    @Transactional
    public T updateById(T entity) {
        boolean success = SqlHelper.retBool(this.mapper.updateById(entity));
        if (!success) {
            log.error("实体信息：" + entity.toString());
            throw new DataCrudException("数据更新失败！");
        }

        return this.getById(entity.getId());
    }

    @Transactional
    public T saveOrUpdate(T entity) {
        if (entity.getId() == null) {
            return this.insert(entity);
        } else {
            return this.updateById(entity);
        }
    }

    @Transactional
    public void delete(Long id) {
        Assert.notNull(id, "ID");

        boolean success = SqlHelper.retBool(this.mapper.deleteById(id));
        if (!success) {
            log.error("实体ID：" + id);
            throw new DataCrudException("数据删除失败！");
        }
    }

    @Transactional
    public void delete(T entity) {
        Assert.notNull(entity.getId(), "ID");
        this.delete(entity.getId());
    }

    @Transactional
    public void delete(Wrapper<T> wrapper) {
        this.mapper.delete(wrapper);
    }

    public T getById(Long id) {
        return this.mapper.selectById(id);
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

    public Page<Map<String, Object>> queryMaps(Page<Map<String, Object>> page, Wrapper<T> queryWrapper) {
        if (page.orders().size() == 0) {
            page.orders().add(new OrderItem(DEFAULT_ORDER_COLUMN, false));
        }
        return mapper.selectMapsPage(page, queryWrapper);
    }

    public List<T> list() {
        return this.list(Wrappers.emptyWrapper());
    }

    public List<T> list(Wrapper<T> queryWrapper) {
        return mapper.selectList(queryWrapper);
    }

    public List<T> listByMap(Map<String, Object> columnMap) {
        return mapper.selectByMap(columnMap);
    }

    public List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper) {
        return mapper.selectMaps(queryWrapper);
    }

    public int count(Wrapper<T> queryWrapper) {
        return SqlHelper.retCount(mapper.selectCount(queryWrapper));
    }

    public boolean exists(Wrapper<T> queryWrapper) {
        return this.count(queryWrapper) > 0;
    }

    @SuppressWarnings("unchecked")
    public <N> List<N> listObjects(Wrapper<T> queryWrapper) {
        List<Object> objects = mapper.selectObjs(queryWrapper);
        return (List<N>) objects;
    }
}
