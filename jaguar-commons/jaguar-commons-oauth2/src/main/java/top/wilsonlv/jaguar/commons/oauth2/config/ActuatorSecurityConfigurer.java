package top.wilsonlv.jaguar.commons.oauth2.config;

import de.codecentric.boot.admin.client.config.ClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import top.wilsonlv.jaguar.commons.oauth2.component.JaguarAccessDeniedHandler;
import top.wilsonlv.jaguar.commons.oauth2.util.MonitorUitl;

/**
 * @author lvws
 * @since 2021/7/26
 */
@Order(1)
@Configuration
@RequiredArgsConstructor
public class ActuatorSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final JaguarAccessDeniedHandler accessDeniedHandler;

    private final ClientProperties clientProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/actuator").antMatcher("/actuator/**")
                .authorizeRequests()
                .anyRequest()
                .hasIpAddress(MonitorUitl.getMonitorIp(clientProperties.getAdminUrl()))
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and().cors()
                .and().csrf().disable()
                .headers().frameOptions().disable();
    }

}
