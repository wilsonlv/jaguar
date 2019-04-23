package com.jaguar.sys.log;

import com.jaguar.sys.log.intercepter.SysLogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Created by lvws on 2019/1/24.
 */
@Component
public class SysLogInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private SysLogInterceptor sysLogInterceptor;

    private static final String PATH_PATTERNS = "/**";

    private static final String[] EXCLUDE_PATH_PATTERNS = new String[]{"/*.ico"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sysLogInterceptor).addPathPatterns(PATH_PATTERNS).excludePathPatterns(EXCLUDE_PATH_PATTERNS);
    }

}
