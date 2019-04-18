package com.itqingning.jaguar.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Created by lvws on 2019/1/24.
 */
@Component
@PropertySource("classpath:interceptor.properties")
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private MaliceIpInterceptor maliceIpInterceptor;
    @Autowired
    private MaliceSessionInterceptor maliceSessionInterceptor;
    @Value("${interceptor.malice.ip.enable}")
    private Boolean ipEnable;
    @Value("${interceptor.malice.session.enable}")
    private Boolean sessionEnable;

    private static final String PATH_PATTERNS = "/**";

    private static final String[] EXCLUDE_PATH_PATTERNS = new String[]{"/*.ico"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (ipEnable) {
            registry.addInterceptor(maliceIpInterceptor).addPathPatterns(PATH_PATTERNS).excludePathPatterns(EXCLUDE_PATH_PATTERNS);
        }
        if (sessionEnable) {
            registry.addInterceptor(maliceSessionInterceptor).addPathPatterns(PATH_PATTERNS).excludePathPatterns(EXCLUDE_PATH_PATTERNS);
        }
    }

}
