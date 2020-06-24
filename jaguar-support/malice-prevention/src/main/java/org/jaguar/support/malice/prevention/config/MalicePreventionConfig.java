package org.jaguar.support.malice.prevention.config;

import org.jaguar.support.malice.prevention.interceptor.MaliceIpInterceptor;
import org.jaguar.support.malice.prevention.interceptor.MaliceSessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author lvws
 * @since 2019/1/24.
 */
@Configuration
public class MalicePreventionConfig implements WebMvcConfigurer {

    private static final String PATH_PATTERNS = "/**";

    @Autowired
    private MaliceIpInterceptor maliceIpInterceptor;
    @Autowired
    private MaliceSessionInterceptor maliceSessionInterceptor;
    @Autowired
    private MalicePreventionProperties malicePreventionProperties;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        if (malicePreventionProperties.getIpEnable()) {
            registry.addInterceptor(maliceIpInterceptor).addPathPatterns(PATH_PATTERNS).order(5);
        }
        if (malicePreventionProperties.getSessionEnable()) {
            registry.addInterceptor(maliceSessionInterceptor).addPathPatterns(PATH_PATTERNS).order(5);
        }
    }

}
