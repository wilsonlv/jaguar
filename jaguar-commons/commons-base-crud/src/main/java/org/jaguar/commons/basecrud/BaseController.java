package org.jaguar.commons.basecrud;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.jaguar.commons.web.JsonResult;
import org.jaguar.commons.web.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 控制器基类
 *
 * @author lvws
 * @version 2016年5月20日 下午3:47:58
 */
@Slf4j
public abstract class BaseController<E extends BaseModel, M extends BaseMapper<E>, S extends BaseService<E, M>> {

    @Autowired
    protected S service;

    public JsonResult<? extends IPage<E>> query(IPage<E> page) {
        return this.query(page, Wrappers.emptyWrapper());
    }

    public JsonResult<IPage<E>> query(IPage<E> page, Wrapper<E> queryWrapper) {
        page = service.query(page, queryWrapper);
        return success(page);
    }

    public JsonResult<List<E>> list() {
        List<E> list = service.list();
        return success(list);
    }

    public JsonResult<E> getById(Long id) {
        E model = service.getById(id);
        return success(model);
    }

    public JsonResult<E> insert(E entity) {
        E model = service.insert(entity);
        return success(model);
    }

    public JsonResult<E> updateById(E entity) {
        E model = service.updateById(entity);
        return success(model);
    }

    public JsonResult<E> saveOrUpdate(E entity) {
        E model = service.saveOrUpdate(entity);
        return success(model);
    }

    public JsonResult<?> delete(Long id) {
        service.delete(id);
        return success();
    }


    protected JsonResult<?> success() {
        return new JsonResult<>(ResultCode.OK, null, JsonResult.SUCCESS_MSG);
    }

    protected <T> JsonResult<T> success(T data) {
        return new JsonResult<>(ResultCode.OK, data, JsonResult.SUCCESS_MSG);
    }

    protected <T> JsonResult<IPage<T>> success(IPage<T> data) {
        return new JsonResult<>(ResultCode.OK, data, JsonResult.SUCCESS_MSG);
    }

}
