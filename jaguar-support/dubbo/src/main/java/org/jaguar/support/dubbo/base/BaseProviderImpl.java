package org.jaguar.support.dubbo.base;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.reflectasm.MethodAccess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jaguar.core.base.BaseModel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * @author lvws
 * @since 2019/5/23.
 */
public abstract class BaseProviderImpl implements ApplicationContextAware, IBaseProvider {

    protected static Logger logger = LogManager.getLogger();

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Parameter execute(Parameter parameter) {
        logger.info("========================================");
        logger.info("开始请求：{}", JSON.toJSONString(parameter));

        long start = System.currentTimeMillis();
        Object service = applicationContext.getBean(parameter.getService());
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
                result = methodAccess.invoke(service, parameter.getMethod(), id);
            } else if (str != null) {
                result = methodAccess.invoke(service, parameter.getMethod(), str);
            } else if (flag != null) {
                result = methodAccess.invoke(service, parameter.getMethod(), flag);
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

            if (response != null) {
                logger.info("完成响应：{}", JSON.toJSONString(result));
            } else {
                logger.info("空响应");
            }

            logger.info("响应时间：{}ms", end - start);
            logger.info("========================================");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
