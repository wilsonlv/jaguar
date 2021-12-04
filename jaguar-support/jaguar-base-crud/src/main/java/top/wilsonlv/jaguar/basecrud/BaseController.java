package top.wilsonlv.jaguar.basecrud;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.wilsonlv.jaguar.commons.web.response.JsonResult;

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

    public JsonResult<? extends Page<E>> query(Page<E> page) {
        return this.query(page, Wrappers.emptyWrapper());
    }

    public JsonResult<Page<E>> query(Page<E> page, Wrapper<E> queryWrapper) {
        page = service.query(page, queryWrapper);
        return success(page);
    }

    public JsonResult<E> getById(Long id) {
        E model = service.getById(id);
        return success(model);
    }

    public JsonResult<Void> insert(E entity) {
        service.insert(entity);
        return success();
    }

    public JsonResult<Void> updateById(E entity) {
        service.updateById(entity);
        return success();
    }

    public JsonResult<Void> saveOrUpdate(E entity) {
        service.saveOrUpdate(entity);
        return success();
    }

    public JsonResult<Void> delete(Long id) {
        service.delete(id);
        return success();
    }

    protected JsonResult<Void> success() {
        return JsonResult.success();
    }

    protected <T> JsonResult<T> success(T data) {
        return JsonResult.success(data);
    }

}
