package top.wilsonlv.jaguar.support.datamodifylog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import top.wilsonlv.jaguar.commons.basecrud.BaseMapper;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;
import top.wilsonlv.jaguar.support.datamodifylog.entity.DataModifyLoggable;

import java.util.Collection;

/**
 * <p>
 * 字段编辑日志表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-04-10
 */
@Slf4j
public abstract class AbstractDataModifyLoggableService<T extends DataModifyLoggable, M extends BaseMapper<T>> extends BaseService<T, M> {

    @Autowired
    private DataModifyLogService<T> dataModifyLogService;

    @Override
    @Transactional
    public void updateById(T entity) {
        super.updateById(entity);

        T org = this.getById(entity.getId());
        try {
            dataModifyLogService.logEdit(org, entity);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new CheckedException(e.getMessage());
        }
    }

    @Override
    public void batchUpdateById(Collection<T> entityList, int batchSize) {
        super.batchUpdateById(entityList, batchSize);

        try {
            for (T t : entityList) {
                dataModifyLogService.logEdit(null, t);
            }
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new CheckedException(e.getMessage());
        }
    }
}
