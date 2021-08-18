package top.wilsonlv.jaguar.support.fieldeditlog.service;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityUser;
import top.wilsonlv.jaguar.commons.oauth2.util.SecurityUtil;
import top.wilsonlv.jaguar.support.fieldeditlog.mapper.FieldEditLogMapper;
import top.wilsonlv.jaguar.support.fieldeditlog.entity.FieldEditLog;
import top.wilsonlv.jaguar.support.fieldeditlog.entity.FieldEditLoggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wilsonlv.jaguar.support.fieldeditlog.FieldEditLogConstant;

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
public class FieldEditLogService<T extends FieldEditLoggable> {

    private static final List<String> FILTER_FIELDS = new ArrayList<String>() {{
        add(FieldEditLogConstant.ID);
        add(FieldEditLogConstant.CREATE_BY);
        add(FieldEditLogConstant.CREATE_BY);
        add(FieldEditLogConstant.CREATE_TIME);
        add(FieldEditLogConstant.UPDATE_BY);
        add(FieldEditLogConstant.UPDATE_BY);
        add(FieldEditLogConstant.UPDATE_TIME);
        add(FieldEditLogConstant.SERIAL_VERSION_UID);
    }};

    @Autowired
    private FieldEditLogMapper fieldEditLogMapper;
    @Autowired
    private GlobalConfig globalConfig;

    @Transactional
    public void logEdit(T persistence, T update) throws IllegalAccessException {
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
                strategy = globalConfig.getDbConfig().getUpdateStrategy();
            }

            if (strategy == FieldStrategy.DEFAULT) {
                strategy = globalConfig.getDbConfig().getUpdateStrategy();
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

            Object oldValue = field.get(persistence);
            if (newValue == null && oldValue == null) {
                //新旧值都为null，值不变
                continue;
            } else if (newValue != null && newValue.equals(oldValue)) {
                //新旧值都不为null，新值等于旧值，值不变
                continue;
            }

            SecurityUser currentUser = SecurityUtil.getCurrentUser();

            FieldEditLog fieldEditLog = new FieldEditLog();
            fieldEditLog.setClassName(update.getClass().getName());
            fieldEditLog.setFieldName(field.getName());
            fieldEditLog.setRecordId(persistence.getId());
            fieldEditLog.setOldValue(String.valueOf(oldValue));
            fieldEditLog.setNewValue(String.valueOf(newValue));

            fieldEditLog.setLastModifyTime(persistence.getUpdateTime());
            fieldEditLog.setLastModifyUserName(persistence.getUpdateBy());
            fieldEditLog.setLastModifyUserId(persistence.getUpdateUserId());
            fieldEditLog.setModifyTime(LocalDateTime.now());
            fieldEditLog.setModifyUserId(currentUser != null ? currentUser.getId() : null);
            fieldEditLog.setModifyUserName(currentUser != null ? currentUser.getUsername() : null);
            fieldEditLogMapper.insert(fieldEditLog);
        }
    }

}
