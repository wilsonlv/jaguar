package top.wilsonlv.jaguar.datamodifylog.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import top.wilsonlv.jaguar.datamodifylog.DataModifyLogConstant;
import top.wilsonlv.jaguar.oauth2.model.SecurityUser;
import top.wilsonlv.jaguar.oauth2.util.SecurityUtil;

import java.time.LocalDateTime;

/**
 * @author lvws
 * @since 2021/6/5
 */
@Primary
@Component
public class DataModifyLoggableHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, DataModifyLogConstant.CREATE_TIME, LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, DataModifyLogConstant.UPDATE_TIME, LocalDateTime::now, LocalDateTime.class);

        SecurityUser currentUser = SecurityUtil.getCurrentUser();
        if (currentUser != null) {
            this.strictInsertFill(metaObject, DataModifyLogConstant.CREATE_USER_ID, currentUser::getId, Long.class);
            this.strictInsertFill(metaObject, DataModifyLogConstant.CREATE_BY, currentUser::getUsername, String.class);
            this.strictInsertFill(metaObject, DataModifyLogConstant.UPDATE_USER_ID, currentUser::getId, Long.class);
            this.strictInsertFill(metaObject, DataModifyLogConstant.UPDATE_BY, currentUser::getUsername, String.class);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, DataModifyLogConstant.UPDATE_TIME, LocalDateTime::now, LocalDateTime.class);

        SecurityUser currentUser = SecurityUtil.getCurrentUser();
        if (currentUser != null) {
            this.strictInsertFill(metaObject, DataModifyLogConstant.UPDATE_USER_ID, currentUser::getId, Long.class);
            this.strictInsertFill(metaObject, DataModifyLogConstant.UPDATE_BY, currentUser::getUsername, String.class);
        }
    }

}
