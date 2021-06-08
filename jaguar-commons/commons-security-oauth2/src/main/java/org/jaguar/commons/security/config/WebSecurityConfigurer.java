package org.jaguar.commons.security.config;


import org.jaguar.commons.security.component.AuthExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author lvws
 * @since 2021/4/2
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private SpringSecurityProperties springSecurityProperties;

    @Autowired
    private AuthExceptionHandler authExceptionHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //不需要认证就可以访问的
                .authorizeRequests().antMatchers(springSecurityProperties.getIgnoreUrls()).permitAll()
                //其余都需要认证
                .anyRequest().authenticated()
                //异常处理
                .and().exceptionHandling().authenticationEntryPoint(authExceptionHandler)
                .and().cors()
                .and().csrf().disable();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @ConditionalOnMissingBean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
