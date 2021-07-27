package org.jaguar.commons.oauth2.config;

import lombok.RequiredArgsConstructor;
import org.jaguar.commons.oauth2.component.JaguarAccessDeniedHandler;
import org.jaguar.commons.web.util.WebUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author lvws
 * @since 2021/7/26
 */
@Order(1)
@Configuration
@RequiredArgsConstructor
public class ActuatorSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final JaguarAccessDeniedHandler accessDeniedHandler;

    private static final String LOCAL_127 = "127.0.0.1";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String monitorIp;
        InetAddress inetAddress = Inet4Address.getByName("jaguar-monitor");
        if (LOCAL_127.equals(inetAddress.getHostAddress())) {
            monitorIp = WebUtil.getInet4Address().getHostAddress();
        } else {
            monitorIp = inetAddress.getHostAddress();
        }

        http.antMatcher("/actuator").antMatcher("/actuator/**")
                .authorizeRequests()
                .anyRequest().hasIpAddress(monitorIp)
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

}
