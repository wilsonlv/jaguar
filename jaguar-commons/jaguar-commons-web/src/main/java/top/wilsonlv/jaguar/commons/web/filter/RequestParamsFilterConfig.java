package top.wilsonlv.jaguar.commons.web.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @author lvws
 * @since 2021/6/24
 */
@Configuration
public class RequestParamsFilterConfig {

    @Bean
    public FilterRegistrationBean<RequestParamsFilter> requestParamsFilterRegistration() {
        FilterRegistrationBean<RequestParamsFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestParamsFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

}
