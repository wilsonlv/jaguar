package top.wilsonlv.jaguar.commons.basecrud;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import top.wilsonlv.jaguar.commons.web.JsonResult;
import top.wilsonlv.jaguar.commons.web.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;

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

    protected JsonResult<Void> success() {
        return JsonResult.success();
    }

    protected <T> JsonResult<T> success(T data) {
        return JsonResult.success(data);
    }

}
