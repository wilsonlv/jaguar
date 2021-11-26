package top.wilsonlv.jaguar.cloud.handlerlog.client.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.wilsonlv.jaguar.cloud.handlerlog.client.interceptor.HandlerLogInterceptor;

import javax.annotation.Resource;

/**
 * @author lvws
 * @since 2019/1/24
 */
@Component
@ConditionalOnProperty(prefix = "jaguar.handler-log", name = "enable", havingValue = "true", matchIfMissing = true)
public class HandlerLogConfig implements WebMvcConfigurer {

    private static final String PATH_PATTERNS = "/**";

    @Resource
    private HandlerLogInterceptor handlerLogInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerLogInterceptor).addPathPatterns(PATH_PATTERNS).order(10);
    }

}
