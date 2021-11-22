package top.wilsonlv.jaguar.support.datamodifylog.service;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityUser;
import top.wilsonlv.jaguar.commons.oauth2.util.SecurityUtil;
import top.wilsonlv.jaguar.support.datamodifylog.DataModifyLogConstant;
import top.wilsonlv.jaguar.support.datamodifylog.entity.DataModifyLog;
import top.wilsonlv.jaguar.support.datamodifylog.entity.DataModifyLoggable;
import top.wilsonlv.jaguar.support.datamodifylog.mapper.DataModifyLogMapper;

import javax.annotation.Resource;
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
@Service
public class DataModifyLogService<T extends DataModifyLoggable> {

    protected Log log = LogFactory.getLog(this.getClass());

    private static final List<String> FILTER_FIELDS = new ArrayList<String>() {{
        add(DataModifyLogConstant.ID);
        add(DataModifyLogConstant.CREATE_BY);
        add(DataModifyLogConstant.CREATE_BY);
        add(DataModifyLogConstant.CREATE_TIME);
        add(DataModifyLogConstant.UPDATE_BY);
        add(DataModifyLogConstant.UPDATE_BY);
        add(DataModifyLogConstant.UPDATE_TIME);
        add(DataModifyLogConstant.SERIAL_VERSION_UID);
    }};

    @Resource
    private DataModifyLogMapper dataModifyLogMapper;
    @Resource
    private MybatisPlusProperties mybatisPlusProperties;

    public FieldStrategy getUpdateStrategy() {
        FieldStrategy updateStrategy = mybatisPlusProperties.getGlobalConfig().getDbConfig().getUpdateStrategy();
        if (updateStrategy == FieldStrategy.DEFAULT) {
            return FieldStrategy.NOT_NULL;
        } else {
            return updateStrategy;
        }
    }

    @Transactional
    public void log(T persistence, T update) throws IllegalAccessException {
        Field[] fields = update.getClass().getDeclaredFields();

        List<DataModifyLog> dataModifyLogList = new ArrayList<>();
        for (Field field : fields) {
            if (FILTER_FIELDS.contains(field.getName())) {
                continue;
            }

            field.setAccessible(true);
            Object newValue = field.get(update);

            TableField annotation = field.getAnnotation(TableField.class);
            FieldStrategy strategy = null;
            if (annotation != null) {
                //过滤临时字段
                if (!annotation.exist()) {
                    continue;
                }
                strategy = annotation.updateStrategy();
            }

            if (strategy == null || strategy == FieldStrategy.DEFAULT) {
                strategy = getUpdateStrategy();
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

            String oldValue = null;
            LocalDateTime lastModifyTime = null;
            Long lastModifyUserId = null;
            String lastModifyUserName = null;
            if (persistence != null) {
                Object oldValueObj = field.get(persistence);
                if (newValue == null && oldValueObj == null) {
                    //新旧值都为null，值不变
                    continue;
                } else if (newValue != null && newValue.equals(oldValueObj)) {
                    //新旧值都不为null，新值等于旧值，值不变
                    continue;
                }

                oldValue = String.valueOf(oldValueObj);
                lastModifyTime = persistence.getUpdateTime();
                lastModifyUserId = persistence.getUpdateUserId();
                lastModifyUserName = persistence.getUpdateBy();
            }

            SecurityUser currentUser = SecurityUtil.getCurrentUser();

            DataModifyLog dataModifyLog = new DataModifyLog();
            dataModifyLog.setClassName(update.getClass().getName());
            dataModifyLog.setFieldName(field.getName());
            dataModifyLog.setRecordId(update.getId());
            dataModifyLog.setOldValue(oldValue);
            dataModifyLog.setNewValue(String.valueOf(newValue));

            dataModifyLog.setLastModifyTime(lastModifyTime);
            dataModifyLog.setLastModifyUserId(lastModifyUserId);
            dataModifyLog.setLastModifyUserName(lastModifyUserName);

            dataModifyLog.setModifyTime(LocalDateTime.now());
            if (currentUser != null) {
                dataModifyLog.setModifyUserId(currentUser.getId());
                dataModifyLog.setModifyUserName(currentUser.getUsername());
            }
            dataModifyLogList.add(dataModifyLog);
        }

        if (dataModifyLogList.size() > 0) {
            String sqlStatement = SqlHelper.getSqlStatement(DataModifyLogMapper.class, SqlMethod.INSERT_ONE);
            SqlHelper.executeBatch(update.getClass(), log, dataModifyLogList, 100,
                    (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
        }
    }

}
