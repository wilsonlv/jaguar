/**
 *
 */
package com.itqingning.jaguar.web.base;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itqingning.core.exception.BaseException;
import com.itqingning.core.http.HttpCode;
import com.itqingning.jaguar.web.JsonResult;
import com.itqingning.jaguar.web.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 控制器基类
 *
 * @author lvws
 * @version 2016年5月20日 下午3:47:58
 */
public abstract class BaseController {

    protected final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * 获取当前用户Id
     */
    protected Long getCurrentUser() {
        return WebUtil.getCurrentUser();
    }

    /**
     * 设置成功响应代码
     */
    protected ResponseEntity<ModelMap> setSuccessJsonResult() {
        return setSuccessJsonResult(null);
    }

    /**
     * 设置成功响应代码
     */
    protected ResponseEntity<ModelMap> setSuccessJsonResult(Object data) {
        return setJsonResult(HttpCode.OK, data);
    }

    /**
     * 设置响应代码
     */
    protected ResponseEntity<ModelMap> setJsonResult(HttpCode code, Object data) {
        JsonResult jsonResult = new JsonResult();
        if (data != null) {
            if (data instanceof Page) {
                Page<?> page = (Page<?>) data;
                jsonResult.setData(page.getRecords());
                jsonResult.setCurrent(page.getCurrent());
                jsonResult.setSize(page.getSize());
                jsonResult.setPages(page.getPages());
                jsonResult.setTotal(page.getTotal());
            } else if (data instanceof List<?>) {
                jsonResult.setData(data);
                Integer size = ((List<?>) data).size();
                jsonResult.setTotal(size.longValue());
            } else {
                jsonResult.setData(data);
            }
        } else {
            jsonResult.setData(new Object());
        }

        jsonResult.setHttpCode(code);
        jsonResult.setMsg(code.msg());
        jsonResult.setTimestamp(System.currentTimeMillis());

        return ResponseEntity.ok(jsonResult);
    }

    /**
     * 异常处理
     */
    @ExceptionHandler(Exception.class)
    public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex)
            throws Exception {
        ex.printStackTrace();
        JsonResult jsonResult = new JsonResult();

        if (ex instanceof BaseException) {
            ((BaseException) ex).handler(jsonResult);
        } else {
            String msg = StringUtils.defaultIfBlank(ex.getMessage(), HttpCode.INTERNAL_SERVER_ERROR.msg());
            jsonResult.setHttpCode(HttpCode.INTERNAL_SERVER_ERROR);
            jsonResult.setMsg(msg);
        }
        jsonResult.setTimestamp(System.currentTimeMillis());
        logger.debug(JSONObject.toJSONString(jsonResult));

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        byte[] bytes = JSONObject.toJSONBytes(jsonResult, SerializerFeature.DisableCircularReferenceDetect);
        response.getOutputStream().write(bytes);
    }
}
