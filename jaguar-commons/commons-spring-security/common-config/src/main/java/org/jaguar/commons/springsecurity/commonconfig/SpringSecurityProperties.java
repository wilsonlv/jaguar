package org.jaguar.commons.springsecurity.commonconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author lvws
 * @since 2021/4/4
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jaguar.spring.security")
public class SpringSecurityProperties {

    private String loginUrl = "/auth/login";

    private String logoutUrl = "/auth/logout";

    private String usernameParameter = UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

    private String passwordParameter = UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;

    private String[] permitAllAntMatchers;

}
