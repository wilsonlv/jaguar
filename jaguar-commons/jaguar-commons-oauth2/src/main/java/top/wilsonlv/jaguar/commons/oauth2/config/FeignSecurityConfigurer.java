package top.wilsonlv.jaguar.commons.oauth2.config;

import lombok.RequiredArgsConstructor;
import top.wilsonlv.jaguar.commons.oauth2.component.JaguarAccessDeniedHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;

/**
 * @author lvws
 * @since 2021/7/26
 */
@Order(2)
@Configuration
@RequiredArgsConstructor
public class FeignSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final JaguarAccessDeniedHandler accessDeniedHandler;

    private final ClientDetailsService clientDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/feign/**")
                .userDetailsService(new ClientDetailsUserDetailsService(clientDetailsService))
                .authorizeRequests().anyRequest().hasAuthority("feign")
                .and().httpBasic()
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

}
