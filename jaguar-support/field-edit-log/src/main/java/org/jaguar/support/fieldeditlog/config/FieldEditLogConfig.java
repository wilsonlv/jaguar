package org.jaguar.support.fieldeditlog.config;

import org.jaguar.support.fieldeditlog.intercepter.FieldEditLogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author lvws
 * @since 2021/3/30
 */
@Component
public class FieldEditLogConfig implements WebMvcConfigurer {

    private static final String PATH_PATTERNS = "/**";

    @Autowired
    private FieldEditLogInterceptor fieldEditLogInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(fieldEditLogInterceptor).addPathPatterns(PATH_PATTERNS).order(50);
    }

}
