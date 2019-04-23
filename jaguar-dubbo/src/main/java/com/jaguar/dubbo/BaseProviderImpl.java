package com.jaguar.dubbo;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.jaguar.core.base.BaseModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.Map;

public abstract class BaseProviderImpl implements ApplicationContextAware, BaseProvider {

    protected static Logger logger = LogManager.getLogger();

    private static final String SAVE_LOG = "saveLog";

    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Parameter execute(Parameter parameter) {
        boolean logPrintEnable = !SAVE_LOG.equals(parameter.getMethod());

        if (logPrintEnable) {
            logger.info("========================================");
            logger.info("开始请求：{}", JSON.toJSONString(parameter));
        }

        long start = System.currentTimeMillis();
        Object service = applicationContext.getBean(parameter.getService());
        try {
            Long id = parameter.getId();
            BaseModel model = parameter.getModel();
            List<?> list = parameter.getList();
            Map<?, ?> map = parameter.getMap();
            Object[] objects = parameter.getObjects();
            String str = parameter.getStr();
            Boolean flag = parameter.getFlag();
            Object obj = parameter.getObject();
            Object result;
            MethodAccess methodAccess = MethodAccess.get(service.getClass());
            if (id != null) {
                result = methodAccess.invoke(service, parameter.getMethod(), id);
            } else if (model != null) {
                result = methodAccess.invoke(service, parameter.getMethod(), model);
            } else if (list != null) {
                result = methodAccess.invoke(service, parameter.getMethod(), list);
            } else if (map != null) {
                result = methodAccess.invoke(service, parameter.getMethod(), map);
            } else if (objects != null) {
                result = methodAccess.invoke(service, parameter.getMethod(), objects);
            } else if (str != null) {
                result = methodAccess.invoke(service, parameter.getMethod(), str);
            } else if (flag != null) {
                result = methodAccess.invoke(service, parameter.getMethod(), flag);
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

            if (logPrintEnable) {
                if (response != null) {
                    logger.info("完成响应：{}", JSON.toJSONString(result));
                } else {
                    logger.info("空响应");
                }

                logger.info("响应时间：{}ms", end - start);
                logger.info("========================================");
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
