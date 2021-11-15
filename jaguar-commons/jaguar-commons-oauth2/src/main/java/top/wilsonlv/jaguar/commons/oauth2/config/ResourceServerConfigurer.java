package top.wilsonlv.jaguar.commons.oauth2.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import top.wilsonlv.jaguar.commons.enums.UserType;
import top.wilsonlv.jaguar.commons.oauth2.component.AuthenticationExceptionHandler;
import top.wilsonlv.jaguar.commons.oauth2.component.JaguarAccessDeniedHandler;
import top.wilsonlv.jaguar.commons.oauth2.properties.SpringSecurityProperties;
import top.wilsonlv.jaguar.commons.oauth2.util.MonitorUitl;

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

    private final TokenStore tokenStore;

    private final SpringSecurityProperties springSecurityProperties;

    private final JaguarAccessDeniedHandler jaguarAccessDeniedHandler;

    private final AuthenticationExceptionHandler authenticationExceptionHandler;


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(applicationName)
                .tokenStore(tokenStore)
                .authenticationEntryPoint(authenticationExceptionHandler);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //不需要认证就可以访问的
                .authorizeRequests()

                .antMatchers("/admin/**").access("#oauth2.hasScope('" + UserType.ADMIN.getUserTypeName() + "')")
                .antMatchers("/tenant/**").access("#oauth2.hasScope('" + UserType.TENANT.getUserTypeName() + "')")
                .antMatchers("/user/**").access("#oauth2.hasScope('" + UserType.USER.getUserTypeName() + "')")

                .antMatchers("/swagger-resources", "/swagger-resources/**", "/v2/**").permitAll()
                .antMatchers("/druid/**").hasIpAddress(MonitorUitl.getMonitorIp());

        if (springSecurityProperties.getIgnoreUrls() != null) {
            for (String ignoreUrl : springSecurityProperties.getIgnoreUrls()) {
                registry.antMatchers(ignoreUrl).permitAll();
            }
        }

        //其余都需要认证
        registry.anyRequest().authenticated()
                .and().exceptionHandling()
                .accessDeniedHandler(jaguarAccessDeniedHandler).authenticationEntryPoint(authenticationExceptionHandler)
                //异常处理
                .and().cors()
                .and().csrf().disable()
                .headers().frameOptions().disable();
    }

}
