package org.jaguar.core.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author lvws
 * @since 2019/5/6
 */
public abstract class AbstractController<T extends BaseModel,
        M extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T>, N extends BaseService<T, M>> extends BaseController {

    @Autowired
    protected N service;

    public ResponseEntity<JsonResult<Page<T>>> query(com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page) {
        return this.query(page, Wrappers.emptyWrapper());
    }

    public ResponseEntity<JsonResult<Page<T>>> query(com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page,
                                                     Wrapper<T> queryWrapper) {
        page = service.query(page, queryWrapper);
        return success(page);
    }

    public ResponseEntity<JsonResult<List<T>>> list() {
        List<T> list = service.list();
        return success(list);
    }

    public ResponseEntity<JsonResult<T>> getById(Long id) {
        T model = service.getById(id);
        return success(model);
    }

    public ResponseEntity<JsonResult<T>> insert(T entity) {
        T model = service.insert(entity);
        return success(model);
    }

    public ResponseEntity<JsonResult<T>> updateById(T entity) {
        T model = service.updateById(entity);
        return success(model);
    }

    public ResponseEntity<JsonResult<T>> saveOrUpdate(T entity) {
        T model = service.saveOrUpdate(entity);
        return success(model);
    }

    public ResponseEntity<JsonResult<?>> delete(Long id) {
        service.delete(id);
        return success();
    }

}