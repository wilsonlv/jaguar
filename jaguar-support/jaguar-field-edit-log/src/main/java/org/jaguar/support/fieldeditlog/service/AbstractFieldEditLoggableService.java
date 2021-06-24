package org.jaguar.support.fieldeditlog.service;

import lombok.extern.slf4j.Slf4j;
import org.jaguar.commons.basecrud.BaseMapper;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.commons.web.exception.impl.CheckedException;
import org.jaguar.support.fieldeditlog.model.FieldEditLoggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 字段编辑日志表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-04-10
 */
@Slf4j
public abstract class AbstractFieldEditLoggableService<T extends FieldEditLoggable, M extends BaseMapper<T>> extends BaseService<T, M> {

    @Autowired
    private FieldEditLogService<T> fieldEditLogService;

    @Override
    @Transactional
    public void updateById(T entity) {
        T org = this.getById(entity.getId());
        try {
            fieldEditLogService.logEdit(org, entity);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new CheckedException(e.getMessage());
        }

        super.updateById(entity);
    }

}
