package org.jaguar.modules.auth.server.config;

import lombok.RequiredArgsConstructor;
import org.jaguar.commons.oauth2.component.AuthenticationExceptionHandler;
import org.jaguar.commons.oauth2.component.JaguarAccessDeniedHandler;
import org.jaguar.modules.auth.server.component.LoginFailureHandler;
import org.jaguar.modules.auth.server.component.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author lvws
 * @since 2021/6/8
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final LoginSuccessHandler loginSuccessHandler;

    private final LoginFailureHandler loginFailureHandler;

    private final AuthenticationExceptionHandler authenticationExceptionHandler;

    private final JaguarAccessDeniedHandler jaguarAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin().loginPage("/401").loginProcessingUrl("/login")
                .successHandler(loginSuccessHandler).failureHandler(loginFailureHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/token", "/oauth/check_token", "/401").permitAll()
                .antMatchers("/swagger-resources", "/swagger-resources/**", "/v2/**").permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling().accessDeniedHandler(jaguarAccessDeniedHandler)
                .and().cors()
                .and().csrf().disable()
                .headers().frameOptions().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("*.html")
                .antMatchers("*.css")
                .antMatchers("*.js")
                .antMatchers("*.png")
                .antMatchers("/swagger-ui/**");
    }
}
