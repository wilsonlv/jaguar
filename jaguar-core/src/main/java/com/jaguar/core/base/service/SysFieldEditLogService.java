package com.jaguar.core.base.service;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jaguar.core.base.BaseModel;
import com.jaguar.core.base.BaseService;
import com.jaguar.core.base.mapper.SysFieldEditLogMapper;
import com.jaguar.core.base.model.SysFieldEditLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字段编辑日志表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-04-10
 */
@Service
public class SysFieldEditLogService {

    private static final String ID = "id";
    private static final String CREATE_BY = "createBy";
    private static final String CREATE_TIME = "createTime";
    private static final String UPDATE_BY = "updateBy";
    private static final String UPDATE_TIME = "updateTime";
    private static final String SERIAL_VERSION_UID = "serialVersionUID";

    private static final List<String> FILTER_FIELDS = new ArrayList<String>() {{
        add(ID);
        add(ID);
        add(CREATE_BY);
        add(CREATE_TIME);
        add(UPDATE_BY);
        add(UPDATE_TIME);
        add(SERIAL_VERSION_UID);
    }};

    @Autowired(required = false)
    private SysFieldEditLogMapper sysFieldEditLogMapper;
    @Value("${mybatis-plus.global-config.db-config.field-strategy}")
    private FieldStrategy fieldStrategy;

    @Transactional
    public <T extends BaseModel> T compareDifference(T org, T update) throws IllegalAccessException, InstantiationException {
        Long recordId = update.getId();
        Long lastUpdateBy = org.getUpdateBy();
        Date lastUpdateTime = org.getUpdateTime();
        Long currentUpdateBy = update.getUpdateBy();
        Date currentUpdateTime = update.getUpdateTime();

        T difference = (T) org.getClass().newInstance();
        difference.setId(recordId);
        difference.setUpdateBy(currentUpdateBy);
        difference.setUpdateTime(currentUpdateTime);

        Field[] fields = update.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (FILTER_FIELDS.contains(field.getName())) {
                continue;
            }

            field.setAccessible(true);
            Object newValue = field.get(update);
            TableField annotation = field.getAnnotation(TableField.class);

            if (!annotation.exist()) {//过滤临时字段
                continue;
            }

            FieldStrategy strategy = annotation.strategy();
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
            } else if (annotation.strategy() == FieldStrategy.NOT_NULL && newValue == null) {
                //如果更新策略是非null更新，新值是null，则不会更新
                continue;
            } else if (annotation.strategy() == FieldStrategy.IGNORED) {
                //如果更新策略是直接更新，则需要设置新值
                field.set(difference, newValue);
            }

            Object oldValue = field.get(org);
            if (newValue == null && oldValue == null) {
                //新旧值都为null，值不变
                continue;
            } else if (newValue != null && oldValue != null && newValue.equals(oldValue)) {
                //新旧值都不为null，新值等于旧值，值不变
                continue;
            }

            SysFieldEditLog sysFieldEditLog = new SysFieldEditLog();
            sysFieldEditLog.setClassName(update.getClass().getName());
            sysFieldEditLog.setFieldName(field.getName());
            sysFieldEditLog.setRecordId(recordId);
            sysFieldEditLog.setOldValue(String.valueOf(oldValue));
            sysFieldEditLog.setNewValue(String.valueOf(newValue));
            sysFieldEditLog.setCreateBy(lastUpdateBy);
            sysFieldEditLog.setCreateTime(lastUpdateTime);
            sysFieldEditLog.setUpdateBy(currentUpdateBy);
            sysFieldEditLog.setUpdateTime(currentUpdateTime);
            sysFieldEditLogMapper.insert(sysFieldEditLog);

            field.set(difference, newValue);
        }

        return difference;
    }

    public Page query(Map<String, Object> param) {
        Page page = BaseService.getPage(param);
        List<SysFieldEditLog> sysLogs = sysFieldEditLogMapper.selectEntityPage(page, param);
        return page.setRecords(sysLogs);
    }

}
