package com.itqingning.jaguar.web.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itqingning.core.base.BaseModel;
import com.itqingning.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 控制器基类
 *
 * @author lvws
 * @since 2019-02-21
 */
public abstract class AbstractController<T extends BaseService> extends BaseController {

    @Autowired
    protected T service;

    public Object query(Map<String, Object> param) {
        Page page = service.query(param);
        return setSuccessJsonResult(page);
    }

    public Object queryList(Map<String, Object> param) {
        List list = service.queryList(param);
        return setSuccessJsonResult(list);
    }

    public Object get(Long id) {
        BaseModel baseModel = service.getById(id);
        return setSuccessJsonResult(baseModel);
    }

    public Object update(BaseModel param) {
        BaseModel baseModel = service.update(param);
        return setSuccessJsonResult(baseModel);
    }

    public Object delete(Long id) {
        Boolean delete = service.delete(id);
        return setSuccessJsonResult(delete);
    }

    public Object del(Long id) {
        Boolean del = service.del(id, getCurrentUser());
        return setSuccessJsonResult(del);
    }
}
