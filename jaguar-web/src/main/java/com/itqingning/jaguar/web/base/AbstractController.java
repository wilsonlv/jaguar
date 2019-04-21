package com.itqingning.jaguar.web.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itqingning.jaguar.core.base.BaseModel;
import com.itqingning.jaguar.core.base.BaseService;
import com.itqingning.jaguar.web.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

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

    public ResponseEntity<JsonResult> query(Map<String, Object> param) {
        Page page = service.query(param);
        return setSuccessJsonResult(page);
    }

    public ResponseEntity<JsonResult> queryList(Map<String, Object> param) {
        List list = service.queryList(param);
        return setSuccessJsonResult(list);
    }

    public ResponseEntity<JsonResult> get(Long id) {
        BaseModel baseModel = service.getById(id);
        return setSuccessJsonResult(baseModel);
    }

    public ResponseEntity<JsonResult> update(BaseModel param) {
        BaseModel baseModel = service.update(param);
        return setSuccessJsonResult(baseModel);
    }

    public ResponseEntity<JsonResult> delete(Long id) {
        Boolean delete = service.delete(id);
        return setSuccessJsonResult(delete);
    }

    public ResponseEntity<JsonResult> del(Long id) {
        Boolean del = service.del(id, getCurrentUser());
        return setSuccessJsonResult(del);
    }
}
