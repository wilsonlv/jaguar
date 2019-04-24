package com.jaguar.core.base.service;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jaguar.core.base.BaseModel;
import com.jaguar.core.base.BaseService;
import com.jaguar.core.base.mapper.SysFieldEditLogMapper;
import com.jaguar.core.base.model.SysFieldEditLog;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional
    public <T extends BaseModel> void save(T org, T update) throws IllegalAccessException {
        Long recordId = update.getId();
        Long lastUpdateBy = org.getUpdateBy();
        Date lastUpdateTime = org.getUpdateTime();
        Long currentUpdateBy = update.getUpdateBy();
        Date currentUpdateTime = update.getUpdateTime();

        Field[] fields = update.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (FILTER_FIELDS.contains(field.getName())) {
                continue;
            }

            field.setAccessible(true);
            Object newValue = field.get(update);
            TableField annotation = field.getAnnotation(TableField.class);

            if (newValue == null && annotation.strategy() != FieldStrategy.IGNORED) {
                continue;
            }
            if (newValue instanceof List || newValue instanceof Map) {
                continue;
            }

            Object oldValue = field.get(org);

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
        }
    }

    public Page query(Map<String, Object> param) {
        Page page = BaseService.getPage(param);
        List<SysFieldEditLog> sysLogs = sysFieldEditLogMapper.selectEntityPage(page, param);
        return page.setRecords(sysLogs);
    }

}
