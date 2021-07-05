package org.jaguar.commons.oauth2.config;

import lombok.RequiredArgsConstructor;
import org.jaguar.commons.oauth2.component.AuthenticationExceptionHandler;
import org.jaguar.commons.oauth2.component.JaguarAccessDeniedHandler;
import org.jaguar.commons.oauth2.properties.SpringSecurityProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author lvws
 * @since 2021/4/8
 */
@ConditionalOnMissingBean(AuthorizationServerConfigurerAdapter.class)
@Configuration
@EnableResourceServer
@RequiredArgsConstructor
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {

    @Value("${spring.application.name}")
    private String applicationName;

    private final SpringSecurityProperties springSecurityProperties;

    private final AuthenticationExceptionHandler authenticationExceptionHandler;

    private final JaguarAccessDeniedHandler jaguarAccessDeniedHandler;

    private final TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(applicationName)
                .tokenStore(tokenStore)
                .authenticationEntryPoint(authenticationExceptionHandler);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //不需要认证就可以访问的
                .authorizeRequests().antMatchers(springSecurityProperties.getIgnoreUrls()).permitAll()
                .antMatchers("/swagger-resources", "/swagger-resources/**", "/v2/**").permitAll()
                .antMatchers("/druid/**").permitAll()
                //其余都需要认证
                .anyRequest()
                .authenticated()
                .and().exceptionHandling().accessDeniedHandler(jaguarAccessDeniedHandler)
                //异常处理
                .and().cors()
                .and().csrf().disable()
                .headers().frameOptions().disable();
    }

}
