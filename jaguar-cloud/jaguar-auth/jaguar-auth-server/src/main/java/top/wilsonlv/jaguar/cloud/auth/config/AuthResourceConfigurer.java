package top.wilsonlv.jaguar.cloud.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import top.wilsonlv.jaguar.cloud.auth.component.CaptchaFilter;
import top.wilsonlv.jaguar.cloud.auth.component.LoginFailureHandler;
import top.wilsonlv.jaguar.cloud.auth.component.LoginSuccessHandler;
import top.wilsonlv.jaguar.commons.oauth2.component.AuthenticationExceptionHandler;
import top.wilsonlv.jaguar.commons.oauth2.component.JaguarAccessDeniedHandler;

/**
 * @author lvws
 * @since 2021/4/8
 */
@Configuration
@EnableResourceServer
@RequiredArgsConstructor
public class AuthResourceConfigurer extends ResourceServerConfigurerAdapter {

    @Value("${spring.application.name}")
    private String applicationName;

    private final TokenStore tokenStore;

    private final CaptchaFilter captchaFilter;

    private final LoginSuccessHandler loginSuccessHandler;

    private final LoginFailureHandler loginFailureHandler;

    private final AuthenticationExceptionHandler authenticationExceptionHandler;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(applicationName)
                .tokenStore(tokenStore)
                .authenticationEntryPoint(authenticationExceptionHandler);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin().loginProcessingUrl("/login")
                .successHandler(loginSuccessHandler).failureHandler(loginFailureHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/captcha/**", "/error").permitAll()
                .antMatchers("/oauth/token", "/oauth/check_token").permitAll()
                .antMatchers("/swagger-resources", "/swagger-resources/**", "/v2/**").permitAll()
                .anyRequest().authenticated();
    }

}
