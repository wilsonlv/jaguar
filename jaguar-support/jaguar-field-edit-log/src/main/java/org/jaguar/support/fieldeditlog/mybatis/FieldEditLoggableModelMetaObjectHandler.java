package org.jaguar.support.fieldeditlog.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.jaguar.commons.oauth2.model.SecurityUser;
import org.jaguar.commons.oauth2.util.SecurityUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static org.jaguar.support.fieldeditlog.FieldEditLogConstant.*;

/**
 * @author lvws
 * @since 2021/6/5
 */
@Primary
@Component
public class FieldEditLoggableModelMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, CREATE_TIME, LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime::now, LocalDateTime.class);

        SecurityUser currentUser = SecurityUtil.getCurrentUser();
        if (currentUser != null) {
            this.strictInsertFill(metaObject, CREATE_USER_ID, currentUser::getId, Long.class);
            this.strictInsertFill(metaObject, CREATE_BY, currentUser::getUsername, String.class);
            this.strictInsertFill(metaObject, UPDATE_USER_ID, currentUser::getId, Long.class);
            this.strictInsertFill(metaObject, UPDATE_BY, currentUser::getUsername, String.class);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime::now, LocalDateTime.class);

        SecurityUser currentUser = SecurityUtil.getCurrentUser();
        if (currentUser != null) {
            this.strictInsertFill(metaObject, UPDATE_USER_ID, currentUser::getId, Long.class);
            this.strictInsertFill(metaObject, UPDATE_BY, currentUser::getUsername, String.class);
        }
    }

}
