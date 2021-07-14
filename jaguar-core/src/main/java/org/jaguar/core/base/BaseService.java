package org.jaguar.core.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.jaguar.core.base.service.FieldEditLogService;
import org.jaguar.core.exception.Assert;
import org.jaguar.core.exception.CheckedException;
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
public abstract class BaseService<T extends BaseModel, M extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T>> {

    @Autowired
    protected M mapper;
    @Autowired
    private FieldEditLogService fieldEditLogService;

    public static final String DEFAULT_ORDER_COLUMN = "id_";

    /**
     * 通过SysLogInterceptor日志拦截器来设置
     */
    public static final ThreadLocal<Long> CURRENT_USER = new ThreadLocal<>();

    private long getCurrentUser() {
        Long currentUser = CURRENT_USER.get();
        return currentUser != null ? currentUser : 0L;
    }

    @Transactional
    public T insert(T entity) {
        long currentUser = this.getCurrentUser();

        LocalDateTime now = LocalDateTime.now();
        entity.setId(null);
        entity.setCreateBy(currentUser);
        entity.setCreateTime(now);
        entity.setUpdateBy(currentUser);
        entity.setUpdateTime(now);

        boolean success = SqlHelper.retBool(mapper.insert(entity));
        if (!success) {
            log.error("实体信息：" + entity.toString());
            throw new CheckedException("数据插入失败！");
        }

        return this.getById(entity.getId());
    }

    @Transactional
    public T updateById(T entity) {
        long currentUser = this.getCurrentUser();
        entity.setUpdateBy(currentUser);
        entity.setUpdateTime(LocalDateTime.now());

        T org = this.getById(entity.getId());
        Assert.validateId(org, "实体", entity.getId());

        try {
            fieldEditLogService.logUpdation(org, entity);
        } catch (IllegalAccessException e) {
            throw new CheckedException(e);
        }

        boolean success = SqlHelper.retBool(mapper.updateById(entity));
        if (!success) {
            log.error("实体信息：" + entity.toString());
            throw new CheckedException("数据更新失败！");
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
        T entity = this.getById(id);
        if (entity == null) {
            throw new CheckedException("无效的ID：" + id);
        }
        this.delete(entity);
    }

    @Transactional
    public void delete(T entity) {
        if (entity.getId() == null) {
            throw new CheckedException("无法删除ID为空的实体");
        }

        long currentUser = this.getCurrentUser();
        entity.setUpdateBy(currentUser);
        entity.setUpdateTime(LocalDateTime.now());
        this.updateById(entity);

        boolean success = SqlHelper.delBool(mapper.deleteById(entity.getId()));
        if (!success) {
            log.error("实体ID：" + entity.getId());
            throw new CheckedException("数据删除失败！");
        }
    }

    @Transactional
    public void delete(Wrapper<T> wrapper) {
        List<T> entitys = mapper.selectList(wrapper);
        for (T entity : entitys) {
            this.delete(entity.getId());
        }
    }

    public T getById(Long id) {
        return mapper.selectById(id);
    }

    public T unique(Wrapper<T> queryWrapper) {
        return this.unique(queryWrapper, true);
    }

    public T unique(Wrapper<T> queryWrapper, boolean throwEx) {
        if (throwEx) {
            return mapper.selectOne(queryWrapper);
        }
        return SqlHelper.getObject(mapper.selectList(queryWrapper));
    }

    public Page<T> query(Page<T> page, Wrapper<T> queryWrapper) {
        if (ArrayUtils.isEmpty(page.ascs()) && ArrayUtils.isEmpty(page.descs())) {
            page.setDesc(DEFAULT_ORDER_COLUMN);
        }
        return (Page<T>) mapper.selectPage(page, queryWrapper);
    }

    public Page<Map<String, Object>> queryMaps(Page<T> page, Wrapper<T> queryWrapper) {
        if (ArrayUtils.isEmpty(page.ascs()) && ArrayUtils.isEmpty(page.descs())) {
            page.setDesc(DEFAULT_ORDER_COLUMN);
        }
        return (Page<Map<String, Object>>) mapper.selectMapsPage(page, queryWrapper);
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