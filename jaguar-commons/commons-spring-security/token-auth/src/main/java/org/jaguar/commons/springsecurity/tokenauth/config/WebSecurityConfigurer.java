package org.jaguar.commons.springsecurity.tokenauth.config;

import org.jaguar.commons.springsecurity.commonconfig.SpringSecurityProperties;
import org.jaguar.commons.springsecurity.commonconfig.handler.UnAuthExceptionHandler;
import org.jaguar.commons.springsecurity.tokenauth.TokenFactory;
import org.jaguar.commons.springsecurity.tokenauth.filter.TokenAuthFilter;
import org.jaguar.commons.springsecurity.tokenauth.handler.TokenLoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.Serializable;

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
    private SecurityTokenProperties securityTokenProperties;

    @Autowired
    private UnAuthExceptionHandler unAuthExceptionHandler;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    private TokenFactory tokenFactory;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterAt(tokenAuthFilter(), BasicAuthenticationFilter.class);

        TokenLoginSuccessHandler successHandler = securityTokenProperties.getSuccessHandler()
                .getConstructor(TokenFactory.class, RedisTemplate.class, SecurityTokenProperties.class)
                .newInstance(tokenFactory, redisTemplate, securityTokenProperties);
        AuthenticationFailureHandler failureHandler = securityTokenProperties.getFailureHandler().newInstance();

        http
                .formLogin().loginProcessingUrl(springSecurityProperties.getLoginUrl())
                .usernameParameter(springSecurityProperties.getUsernameParameter())
                .passwordParameter(springSecurityProperties.getPasswordParameter())
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and().logout().logoutUrl(springSecurityProperties.getLogoutUrl())
                //不需要认证就可以访问的
                .and().authorizeRequests().antMatchers(springSecurityProperties.getPermitAllAntMatchers()).permitAll()
                //其余都需要认证
                .anyRequest().authenticated()
                //异常处理
                .and().exceptionHandling().authenticationEntryPoint(unAuthExceptionHandler)
                .and().cors()
                .and().csrf().disable();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    public TokenAuthFilter tokenAuthFilter() throws Exception {
        return new TokenAuthFilter(authenticationManager(), unAuthExceptionHandler, redisTemplate, tokenFactory);
    }

}
