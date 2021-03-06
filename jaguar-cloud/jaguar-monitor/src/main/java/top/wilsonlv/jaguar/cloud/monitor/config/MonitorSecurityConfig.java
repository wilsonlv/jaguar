package top.wilsonlv.jaguar.cloud.monitor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.util.WebUtils;
import top.wilsonlv.jaguar.commons.web.util.WebUtil;

import java.net.Inet4Address;
import java.net.InetAddress;

/**
 * @author lvws
 * @since 2021/6/23
 */
@Configuration
public class MonitorSecurityConfig {

    @Order(1)
    @Configuration
    static class ActuatorSecurityConfigurer extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            String localIp = WebUtil.getInet4Address().getHostAddress();

            http.antMatcher("/actuator").antMatcher("/actuator/**")
                    .authorizeRequests()
                    .anyRequest().access("isAuthenticated() or hasIpAddress('" + localIp + "')")
                    .and().httpBasic();
        }
    }

    @Order(2)
    @Configuration
    static class SecurityConfigurer extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
            successHandler.setTargetUrlParameter("redirectTo");
            successHandler.setDefaultTargetUrl("/");
            http.authorizeRequests()

                    //1.配置所有静态资源和登录页可以公开访问
                    .antMatchers("/assets/**").permitAll()
                    .antMatchers("/login").permitAll()
                    .anyRequest().authenticated()
                    .and()

                    //2.配置登录和登出路径
                    .formLogin().loginPage("/login").successHandler(successHandler).and()
                    .logout().logoutUrl("/logout").and()

                    //3.开启http basic支持，admin-client注册时需要使用
                    .httpBasic().and()
                    .csrf()

                    //4.开启基于cookie的csrf保护
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

                    //5.忽略这些路径的csrf保护以便admin-client注册
                    .ignoringAntMatchers("/instances", "/actuator/**");
        }
    }

}
