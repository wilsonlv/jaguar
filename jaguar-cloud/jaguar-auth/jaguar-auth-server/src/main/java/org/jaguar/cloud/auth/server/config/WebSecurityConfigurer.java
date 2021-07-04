package org.jaguar.cloud.auth.server.config;

import lombok.RequiredArgsConstructor;
import org.jaguar.cloud.auth.server.component.CaptchaFilter;
import org.jaguar.cloud.auth.server.component.LoginFailureHandler;
import org.jaguar.cloud.auth.server.component.LoginSuccessHandler;
import org.jaguar.commons.oauth2.component.AuthenticationExceptionHandler;
import org.jaguar.commons.oauth2.component.JaguarAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author lvws
 * @since 2021/6/8
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final CaptchaFilter captchaFilter;

    private final LoginSuccessHandler loginSuccessHandler;

    private final LoginFailureHandler loginFailureHandler;

    private final JaguarAccessDeniedHandler jaguarAccessDeniedHandler;

    private final AuthenticationExceptionHandler authenticationExceptionHandler;

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
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin().loginProcessingUrl("/login")
                .successHandler(loginSuccessHandler).failureHandler(loginFailureHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/token", "/oauth/check_token").permitAll()
                .antMatchers("/captcha/**").permitAll()
                .antMatchers("/swagger-resources", "/swagger-resources/**", "/v2/**").permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling()
                .accessDeniedHandler(jaguarAccessDeniedHandler).authenticationEntryPoint(authenticationExceptionHandler)
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
