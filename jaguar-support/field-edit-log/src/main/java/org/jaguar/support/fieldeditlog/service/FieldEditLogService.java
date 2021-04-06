package org.jaguar.support.fieldeditlog.service;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.core.exception.CheckedException;
import org.jaguar.core.exception.DataCrudException;
import org.jaguar.support.fieldeditlog.FieldEditLogable;
import org.jaguar.support.fieldeditlog.mapper.FieldEditLogMapper;
import org.jaguar.support.fieldeditlog.model.FieldEditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 字段编辑日志表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-04-10
 */
@Slf4j
@Service
public class FieldEditLogService<T extends FieldEditLogable, M extends BaseMapper<T>> extends BaseService<T, M> {

    private static final String ID = "id";
    private static final String CREATE_BY = "createBy";
    private static final String CREATE_TIME = "createTime";
    private static final String UPDATE_BY = "updateBy";
    private static final String UPDATE_TIME = "updateTime";
    private static final String SERIAL_VERSION_UID = "serialVersionUID";

    private static final List<String> FILTER_FIELDS = new ArrayList<String>() {{
        add(ID);
        add(CREATE_BY);
        add(CREATE_TIME);
        add(UPDATE_BY);
        add(UPDATE_TIME);
        add(SERIAL_VERSION_UID);
    }};

    /**
     * 通过FieldEditLogInterceptor日志拦截器来设置
     */
    public static final ThreadLocal<Long> CURRENT_USER = new ThreadLocal<>();

    public long getCurrentUser() {
        Long currentUser = CURRENT_USER.get();
        return currentUser != null ? currentUser : 0L;
    }

    @Autowired
    private FieldEditLogMapper fieldEditLogMapper;
    @Value("${mybatis-plus.global-config.db-config.update-strategy}")
    private FieldStrategy fieldStrategy;

    @Transactional
    public void logEidt(T org, T update) throws IllegalAccessException {
        Long recordId = update.getId();
        Long lastUpdateBy = org.getUpdateBy();
        LocalDateTime lastUpdateTime = org.getUpdateTime();
        Long currentUpdateBy = update.getUpdateBy();
        LocalDateTime currentUpdateTime = update.getUpdateTime();

        Field[] fields = update.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (FILTER_FIELDS.contains(field.getName())) {
                continue;
            }

            field.setAccessible(true);
            Object newValue = field.get(update);

            TableField annotation = field.getAnnotation(TableField.class);
            FieldStrategy strategy;
            if (annotation != null) {
                //过滤临时字段
                if (!annotation.exist()) {
                    continue;
                }
                strategy = annotation.updateStrategy();
            } else {
                strategy = fieldStrategy;
            }

            if (strategy == FieldStrategy.DEFAULT) {
                strategy = fieldStrategy;
            }

            if (strategy == FieldStrategy.NOT_EMPTY) {
                //如果更新策略是非空更新
                if (newValue instanceof String && StringUtils.isBlank((String) newValue)) {
                    //如果是空字符串，则不会更新
                    continue;
                } else if (newValue == null) {
                    //如果是nul，也不会更新
                    continue;
                }
            } else if (strategy == FieldStrategy.NOT_NULL && newValue == null) {
                //如果更新策略是非null更新，新值是null，则不会更新
                continue;
            }

            Object oldValue = field.get(org);
            if (newValue == null && oldValue == null) {
                //新旧值都为null，值不变
                continue;
            } else if (newValue != null && newValue.equals(oldValue)) {
                //新旧值都不为null，新值等于旧值，值不变
                continue;
            }

            FieldEditLog fieldEditLog = new FieldEditLog();
            fieldEditLog.setClassName(update.getClass().getName());
            fieldEditLog.setFieldName(field.getName());
            fieldEditLog.setRecordId(recordId);
            fieldEditLog.setOldValue(String.valueOf(oldValue));
            fieldEditLog.setNewValue(String.valueOf(newValue));
            fieldEditLog.setCreateBy(lastUpdateBy);
            fieldEditLog.setCreateTime(lastUpdateTime);
            fieldEditLog.setUpdateBy(currentUpdateBy);
            fieldEditLog.setUpdateTime(currentUpdateTime);
            fieldEditLogMapper.insert(fieldEditLog);
        }
    }

    @Override
    @Transactional
    public T insert(T entity) {
        long currentUser = getCurrentUser();

        LocalDateTime now = LocalDateTime.now();
        entity.setCreateBy(currentUser);
        entity.setCreateTime(now);
        entity.setUpdateBy(currentUser);
        entity.setUpdateTime(now);

        boolean success = SqlHelper.retBool(this.mapper.insert(entity));
        if (!success) {
            log.error("数据插入失败，实体信息：" + entity.toString());
            throw new DataCrudException("数据插入失败！");
        }

        return this.getById(entity.getId());
    }

    @Override
    @Transactional
    public T updateById(T entity) {
        entity.setUpdateBy(this.getCurrentUser());
        entity.setUpdateTime(LocalDateTime.now());

        T org = this.getById(entity.getId());
        try {
            this.logEidt(org, entity);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new CheckedException(e.getMessage());
        }

        boolean success = SqlHelper.retBool(this.mapper.updateById(entity));
        if (!success) {
            log.error("数据更新失败，实体信息：" + entity.toString());
            throw new DataCrudException("数据更新失败！");
        }

        return this.getById(entity.getId());
    }

    @Override
    @Transactional
    public T saveOrUpdate(T entity) {
        if (entity.getId() == null) {
            return this.insert(entity);
        } else {
            return this.updateById(entity);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        T entity = this.getById(id);
        if (entity == null) {
            throw new DataCrudException("无效的ID：" + id);
        }
        entity.setUpdateBy(this.getCurrentUser());
        entity.setUpdateTime(LocalDateTime.now());
        this.updateById(entity);

        boolean success = SqlHelper.retBool(mapper.deleteById(id));
        if (!success) {
            log.error("数据删除失败，实体ID：" + id);
            throw new DataCrudException("数据删除失败！");
        }
    }

    @Override
    @Transactional
    public void delete(Wrapper<T> wrapper) {
        List<T> entitys = mapper.selectList(wrapper);
        for (T entity : entitys) {
            this.delete(entity.getId());
        }
    }


}
