package org.jaguar.support.dubbo.base;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.reflectasm.MethodAccess;
import lombok.extern.slf4j.Slf4j;
import org.jaguar.core.base.BaseModel;
import org.jaguar.core.base.BaseService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * @author lvws
 * @since 2019/5/23.
 */
@Slf4j
public abstract class BaseProviderImpl implements ApplicationContextAware, IBaseProvider {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Parameter execute(Parameter parameter) {
        log.info("========================================");
        String requestParams = JSON.toJSONString(parameter);
        log.info("开始请求：{}", requestParams);

        long start = System.currentTimeMillis();
        Object service = applicationContext.getBean(parameter.getService());
        BaseService.CURRENT_USER.set(parameter.getCurrentUserId());

        try {
            Long id = parameter.getId();
            String str = parameter.getStr();
            Boolean flag = parameter.getFlag();
            BaseModel model = parameter.getModel();
            List<?> list = parameter.getList();
            Map<?, ?> map = parameter.getMap();
            Object[] objects = parameter.getObjects();
            Object obj = parameter.getObject();
            Object result;
            MethodAccess methodAccess = MethodAccess.get(service.getClass());
            if (id != null) {
                result = methodAccess.invoke(service, parameter.getMethod(), new Class[]{Long.class}, id);
            } else if (str != null) {
                result = methodAccess.invoke(service, parameter.getMethod(), new Class[]{String.class}, str);
            } else if (flag != null) {
                result = methodAccess.invoke(service, parameter.getMethod(), new Class[]{Boolean.class}, flag);
            } else if (model != null) {
                result = methodAccess.invoke(service, parameter.getMethod(), model);
            } else if (list != null) {
                result = methodAccess.invoke(service, parameter.getMethod(), list);
            } else if (map != null) {
                result = methodAccess.invoke(service, parameter.getMethod(), map);
            } else if (objects != null) {
                result = methodAccess.invoke(service, parameter.getMethod(), objects);
            } else if (obj != null) {
                result = methodAccess.invoke(service, parameter.getMethod(), obj);
            } else {
                result = methodAccess.invoke(service, parameter.getMethod());
            }

            long end = System.currentTimeMillis();

            Parameter response = null;
            if (result != null) {
                response = new Parameter(result);
            }

            if (log.isDebugEnabled()) {
                if (response != null) {
                    log.debug("完成响应：{}", requestParams);
                } else {
                    log.debug("空响应");
                }
            }

            log.info("响应时间：{}ms", end - start);
            log.info("========================================");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            BaseService.CURRENT_USER.remove();
        }
    }
}
