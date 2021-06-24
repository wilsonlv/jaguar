package org.jaguar.commons.web.filter;

import org.jaguar.commons.web.filter.RequestParamsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lvws
 * @since 2021/6/24
 */
@Configuration
public class RequestParamsFilterConfig {

    @Bean
    public FilterRegistrationBean<RequestParamsFilter> testFilterRegistration() {
        FilterRegistrationBean<RequestParamsFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestParamsFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }

}
