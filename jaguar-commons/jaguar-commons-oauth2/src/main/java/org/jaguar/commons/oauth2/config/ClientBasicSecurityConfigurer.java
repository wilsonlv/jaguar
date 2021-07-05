package org.jaguar.commons.oauth2.config;

import lombok.RequiredArgsConstructor;
import org.jaguar.commons.oauth2.component.AuthenticationExceptionHandler;
import org.jaguar.commons.oauth2.component.ClientBasicAuthServiceImpl;
import org.jaguar.commons.oauth2.component.JaguarAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static JaguarAccessDeniedHandler JAGUAR_ACCESS_DENIED_HANDLER;

    private static AuthenticationExceptionHandler AUTHENTICATION_EXCEPTION_HANDLER;


    protected static void configureClientBasic(HttpSecurity http, JaguarAccessDeniedHandler accessDeniedHandler,
                                               AuthenticationExceptionHandler exceptionHandler) throws Exception {

        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and().httpBasic().authenticationEntryPoint(exceptionHandler);
    }

    @Order(1)
    @Configuration
    @RequiredArgsConstructor
    static class ActuatorSecurityConfigurer extends WebSecurityConfigurerAdapter {

        @Autowired
        public void setJaguarAccessDeniedHandler(JaguarAccessDeniedHandler accessDeniedHandler) {
            JAGUAR_ACCESS_DENIED_HANDLER = accessDeniedHandler;
        }

        @Autowired
        public void setAuthenticationExceptionHandler(AuthenticationExceptionHandler authenticationExceptionHandler) {
            AUTHENTICATION_EXCEPTION_HANDLER = authenticationExceptionHandler;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/actuator").antMatcher("/actuator/**")
                    .userDetailsService(new ClientBasicAuthServiceImpl("actuator"));
            configureClientBasic(http, JAGUAR_ACCESS_DENIED_HANDLER, AUTHENTICATION_EXCEPTION_HANDLER);
        }
    }

    @Order(2)
    @Configuration
    @RequiredArgsConstructor
    static class FeignSecurityConfigurer extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/feign/**")
                    .userDetailsService(new ClientBasicAuthServiceImpl("feign"));
            configureClientBasic(http, JAGUAR_ACCESS_DENIED_HANDLER, AUTHENTICATION_EXCEPTION_HANDLER);
        }
    }

}
