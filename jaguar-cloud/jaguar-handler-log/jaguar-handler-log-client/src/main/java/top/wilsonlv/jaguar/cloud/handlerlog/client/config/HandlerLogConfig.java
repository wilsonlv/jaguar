package top.wilsonlv.jaguar.cloud.handlerlog.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.wilsonlv.jaguar.cloud.handlerlog.client.interceptor.HandlerLogInterceptor;
import top.wilsonlv.jaguar.cloud.handlerlog.client.properties.HandlerLogProperties;

import javax.annotation.Resource;


/**
 * @author lvws
 * @since 2019/1/24
 */
@Component
public class HandlerLogConfig implements WebMvcConfigurer {

    private static final String PATH_PATTERNS = "/**";

    @Resource
    private HandlerLogProperties handlerLogProperties;

    @Autowired(required = false)
    private HandlerLogInterceptor handlerLogInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (handlerLogProperties.getEnable()) {
            registry.addInterceptor(handlerLogInterceptor).addPathPatterns(PATH_PATTERNS).order(10);
        }
    }

}
