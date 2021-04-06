package org.jaguar.commons.springsecurity.commonconfig;

import org.jaguar.commons.springsecurity.commonconfig.handler.AuthExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author lvws
 * @since 2021/4/4
 */
@Configuration
public class SpringSecurityConfig {

    @Bean
    public AuthExceptionHandler authenticationExceptionHandler() {
        return new AuthExceptionHandler();
    }

    @ConditionalOnMissingBean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
