package com.itqingning.jaguar.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.FunctionLoader;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import com.itqingning.core.exception.BusinessException;
import com.itqingning.core.ioc.SpringContext;
import com.itqingning.core.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * Created by lvws on 2019/3/8.
 */
public class SpringContextFunctionLoader implements FunctionLoader {

    @Override
    public AviatorFunction onFunctionNotFound(String expression) {
        if (StringUtils.isBlank(expression)) {
            return null;
        }

        String[] split = expression.split("\\.");
        if (split.length != 2) {
            return null;
        }

        Object bean = SpringContext.getApplicationContext().getBean(split[0]);

        Method method = null;
        Method[] methods = bean.getClass().getMethods();
        for (Method m : methods) {
            if (m.getName().equals(split[1])) {
                method = m;
                break;
            }
        }
        if (method == null) {
            throw new RuntimeException("method not found:" + split[1]);
        }

        Method finalMethod = method;
        return new AbstractFunction() {

            @Override
            public AviatorObject call(Map<String, Object> env) {
                return new AviatorRuntimeJavaType(invoke());
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
                return new AviatorRuntimeJavaType(invoke(arg1.getValue(env)));
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
                return new AviatorRuntimeJavaType(invoke(arg1.getValue(env), arg2.getValue(env)));
            }

            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
                return new AviatorRuntimeJavaType(invoke(arg1.getValue(env), arg2.getValue(env), arg3.getValue(env)));
            }

            private Object invoke(Object... args) {
                try {
                    for (int i = 0; i < args.length; i++) {
                        Class<?> clazz = finalMethod.getParameterTypes()[i];
                        if (clazz == Date.class) {
                            args[i] = DateUtil.autoParse((String) args[i]);
                        }
                    }

                    return finalMethod.invoke(bean, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.fillInStackTrace();
                    throw new BusinessException(e);
                }
            }

            @Override
            public String getName() {
                return expression;
            }
        };
    }
}
