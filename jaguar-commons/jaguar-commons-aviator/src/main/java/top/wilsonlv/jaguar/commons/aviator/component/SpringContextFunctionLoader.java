package top.wilsonlv.jaguar.commons.aviator.component;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.FunctionLoader;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author lvws
 * @since 2019/3/7.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SpringContextFunctionLoader implements FunctionLoader, InitializingBean {

    private final ApplicationContext applicationContext;

    @Override
    public AviatorFunction onFunctionNotFound(String expression) {
        if (!StringUtils.hasText(expression)) {
            return null;
        }

        String[] split = expression.split("\\.");
        if (split.length != 2) {
            return null;
        }

        Object bean = applicationContext.getBean(split[0]);

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

                        if (clazz == LocalDate.class) {
                            args[i] = LocalDate.parse((CharSequence) args[i]);
                        } else if (clazz == LocalDateTime.class) {
                            args[i] = LocalDateTime.parse((CharSequence) args[i]);
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }

                    return finalMethod.invoke(bean, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.fillInStackTrace();
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String getName() {
                return expression;
            }
        };
    }

    @Override
    public void afterPropertiesSet() {
        log.debug("aviator SpringContextFunctionLoader loading...");
        AviatorEvaluator.addFunctionLoader(this);
        log.debug("aviator SpringContextFunctionLoader loaded");
    }
}
