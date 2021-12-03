package top.wilsonlv.jaguar.commons.oauth2.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import top.wilsonlv.jaguar.commons.oauth2.component.AuthenticationExceptionHandler;
import top.wilsonlv.jaguar.commons.oauth2.component.JaguarAccessDeniedHandler;
import top.wilsonlv.jaguar.commons.oauth2.component.RedisResourceServerServiceImpl;

/**
 * @author lvws
 * @since 2021/7/26
 */
@Order(2)
@Configuration
@RequiredArgsConstructor
@ConditionalOnMissingBean(AuthorizationServerConfigurerAdapter.class)
public class FeignSecurityConfigurer extends WebSecurityConfigurerAdapter {

    public static final String FEIGN_PERMISSION = "feign";

    private final RedisResourceServerServiceImpl resourceServerService;

    private final JaguarAccessDeniedHandler accessDeniedHandler;

    private final AuthenticationExceptionHandler authenticationExceptionHandler;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/feign/**")
                .userDetailsService(resourceServerService)
                .authorizeRequests().anyRequest().hasAuthority(FEIGN_PERMISSION)
                .and().httpBasic().authenticationEntryPoint(authenticationExceptionHandler)

                .and().exceptionHandling()
                .authenticationEntryPoint(authenticationExceptionHandler).accessDeniedHandler(accessDeniedHandler)

                .and().cors()
                .and().csrf().disable()
                .headers().frameOptions().disable();
    }

}
