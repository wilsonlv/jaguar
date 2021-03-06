package top.wilsonlv.jaguar.oauth2.config;

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
import top.wilsonlv.jaguar.oauth2.component.AuthenticationExceptionHandler;
import top.wilsonlv.jaguar.oauth2.component.JaguarAccessDeniedHandler;
import top.wilsonlv.jaguar.oauth2.properties.JaguarSecurityProperties;

import java.util.Map;

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

    private final JaguarSecurityProperties jaguarSecurityProperties;

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
                //?????????????????????????????????
                .authorizeRequests()
                .antMatchers("/swagger-resources", "/swagger-resources/**", "/v2/**").permitAll()
                .antMatchers("/druid/**", "/error").permitAll();

        if (jaguarSecurityProperties.getScopeUrls() != null) {
            for (Map.Entry<String, String> entry : jaguarSecurityProperties.getScopeUrls().entrySet()) {
                registry.antMatchers(entry.getKey()).access("#oauth2.hasScope('" + entry.getValue() + "')");
            }
        }

        if (jaguarSecurityProperties.getIgnoreUrls() != null) {
            for (String ignoreUrl : jaguarSecurityProperties.getIgnoreUrls()) {
                registry.antMatchers(ignoreUrl).permitAll();
            }
        }

        //?????????????????????
        registry.anyRequest().authenticated()
                .and().exceptionHandling()
                .accessDeniedHandler(jaguarAccessDeniedHandler).authenticationEntryPoint(authenticationExceptionHandler)
                //????????????
                .and().cors()
                .and().csrf().disable()
                .headers().frameOptions().disable();
    }

}
