package top.wilsonlv.jaguar.cloud.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import top.wilsonlv.jaguar.cloud.auth.component.CaptchaFilter;
import top.wilsonlv.jaguar.cloud.auth.component.LoginFailureHandler;
import top.wilsonlv.jaguar.cloud.auth.component.LoginSuccessHandler;
import top.wilsonlv.jaguar.cloud.auth.component.LogoutSuccessHandler;
import top.wilsonlv.jaguar.oauth2.component.AuthenticationExceptionHandler;
import top.wilsonlv.jaguar.oauth2.component.JaguarAccessDeniedHandler;

/**
 * @author lvws
 * @since 2021/7/5
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthServerSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final CaptchaFilter captchaFilter;

    private final LoginSuccessHandler loginSuccessHandler;

    private final LoginFailureHandler loginFailureHandler;

    private final LogoutSuccessHandler logoutSuccessHandler;

    private final JaguarAccessDeniedHandler jaguarAccessDeniedHandler;

    private final AuthenticationExceptionHandler authenticationExceptionHandler;

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginProcessingUrl("/login").successHandler(loginSuccessHandler).failureHandler(loginFailureHandler)
                .and()
                .logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/logout", "/captcha/**", "/error").permitAll()
                .antMatchers("/oauth/token", "/oauth/check_token").permitAll()
                .antMatchers("/swagger-resources", "/swagger-resources/**", "/v2/**").permitAll()
                .antMatchers("/error").permitAll()
                .anyRequest().authenticated()

                .and().exceptionHandling()
                .accessDeniedHandler(jaguarAccessDeniedHandler).authenticationEntryPoint(authenticationExceptionHandler)

                .and().cors()
                .and().csrf().disable()
                .headers().frameOptions().disable();
    }

}
