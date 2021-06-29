package org.jaguar.commons.oauth2.config;

import lombok.RequiredArgsConstructor;
import org.jaguar.commons.oauth2.component.AuthenticationExceptionHandler;
import org.jaguar.commons.oauth2.component.JaguarAccessDeniedHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;

/**
 * @author lvws
 * @since 2021/6/23
 */

@Configuration
public class ClientBasicSecurityConfigurer {


    protected static void configureClientBasic(HttpSecurity http, ClientDetailsService clientDetailsService,
                                               JaguarAccessDeniedHandler accessDeniedHandler, AuthenticationExceptionHandler exceptionHandler) throws Exception {

        http
                .userDetailsService(new ClientDetailsUserDetailsService(clientDetailsService))
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and().httpBasic().authenticationEntryPoint(exceptionHandler);
    }

    @Order(1)
    @Configuration
    @RequiredArgsConstructor
    static class ActuatorSecurityConfigurer extends WebSecurityConfigurerAdapter {

        private final ClientDetailsService clientDetailsService;

        private final JaguarAccessDeniedHandler jaguarAccessDeniedHandler;

        private final AuthenticationExceptionHandler authenticationExceptionHandler;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/actuator").antMatcher("/actuator/**");
            configureClientBasic(http, clientDetailsService, jaguarAccessDeniedHandler, authenticationExceptionHandler);
        }
    }

    @Order(2)
    @Configuration
    @RequiredArgsConstructor
    static class FeignSecurityConfigurer extends WebSecurityConfigurerAdapter {

        private final ClientDetailsService clientDetailsService;

        private final JaguarAccessDeniedHandler jaguarAccessDeniedHandler;

        private final AuthenticationExceptionHandler authenticationExceptionHandler;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/feign/**");
            configureClientBasic(http, clientDetailsService, jaguarAccessDeniedHandler, authenticationExceptionHandler);
        }
    }

}
