package com.jaguar.malice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Created by lvws on 2019/1/24.
 */
@Configuration
public class MaliceConfig implements WebMvcConfigurer {

    @Autowired
    private MaliceIpInterceptor maliceIpInterceptor;
    @Autowired
    private MaliceSessionInterceptor maliceSessionInterceptor;
    @Autowired
    private MaliceProperties maliceProperties;

    private static final String PATH_PATTERNS = "/**";

    private static final String[] EXCLUDE_PATH_PATTERNS = new String[]{"/*.ico"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (maliceProperties.getIpEnable()) {
            registry.addInterceptor(maliceIpInterceptor).addPathPatterns(PATH_PATTERNS).excludePathPatterns(EXCLUDE_PATH_PATTERNS);
        }
        if (maliceProperties.getSessionEnable()) {
            registry.addInterceptor(maliceSessionInterceptor).addPathPatterns(PATH_PATTERNS).excludePathPatterns(EXCLUDE_PATH_PATTERNS);
        }
    }

}
