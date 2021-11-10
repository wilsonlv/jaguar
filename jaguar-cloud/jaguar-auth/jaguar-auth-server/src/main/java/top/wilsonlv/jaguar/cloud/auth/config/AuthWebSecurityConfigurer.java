package top.wilsonlv.jaguar.cloud.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import top.wilsonlv.jaguar.commons.oauth2.component.AuthenticationExceptionHandler;
import top.wilsonlv.jaguar.commons.oauth2.component.JaguarAccessDeniedHandler;

/**
 * @author lvws
 * @since 2021/7/5
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

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
                .authorizeRequests()
                .antMatchers("/login", "/captcha/**", "/error").permitAll()
                .antMatchers("/oauth/token", "/oauth/check_token").permitAll()
                .antMatchers("/swagger-resources", "/swagger-resources/**", "/v2/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()

                .and().exceptionHandling()
                .accessDeniedHandler(jaguarAccessDeniedHandler).authenticationEntryPoint(authenticationExceptionHandler)

                .and().cors()
                .and().csrf().disable()
                .headers().frameOptions().disable();
    }

}
