package org.jaguar.commons.oauth2.config;

import lombok.RequiredArgsConstructor;
import org.jaguar.commons.oauth2.properties.SpringSecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author lvws
 * @since 2021/6/23
 */
@Order(1)
@Configuration
@RequiredArgsConstructor
public class ActuatorWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final SpringSecurityProperties springSecurityProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/actuator", "/actuator/**").hasIpAddress(springSecurityProperties.getActuatorAdminIp())
                .anyRequest().authenticated();
    }

}
