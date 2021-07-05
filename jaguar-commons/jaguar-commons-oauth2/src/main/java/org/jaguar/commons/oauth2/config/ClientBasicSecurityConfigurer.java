package org.jaguar.commons.oauth2.config;

import lombok.RequiredArgsConstructor;
import org.jaguar.commons.oauth2.component.AuthenticationExceptionHandler;
import org.jaguar.commons.oauth2.component.ClientBasicAuthServiceImpl;
import org.jaguar.commons.oauth2.component.JaguarAccessDeniedHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author lvws
 * @since 2021/6/23
 */
@Configuration
public class ClientBasicSecurityConfigurer {

    protected static void configureClientBasic(HttpSecurity http, ApplicationContext applicationContext) throws Exception {

        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().exceptionHandling().accessDeniedHandler(applicationContext.getBean(JaguarAccessDeniedHandler.class))
                .and().httpBasic().authenticationEntryPoint(applicationContext.getBean(AuthenticationExceptionHandler.class));
    }

    @Order(1)
    @Configuration
    @RequiredArgsConstructor
    static class ActuatorSecurityConfigurer extends WebSecurityConfigurerAdapter {

        private final ApplicationContext applicationContext;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/actuator").antMatcher("/actuator/**")
                    .userDetailsService(new ClientBasicAuthServiceImpl("actuator"));
            configureClientBasic(http, applicationContext);
        }
    }

    @Order(2)
    @Configuration
    @RequiredArgsConstructor
    static class FeignSecurityConfigurer extends WebSecurityConfigurerAdapter {

        private final ApplicationContext applicationContext;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/feign/**")
                    .userDetailsService(new ClientBasicAuthServiceImpl("feign"));
            configureClientBasic(http, applicationContext);
        }
    }

}
