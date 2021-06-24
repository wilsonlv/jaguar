package org.jaguar.commons.oauth2.config;

import lombok.RequiredArgsConstructor;
import org.jaguar.commons.oauth2.component.AuthenticationExceptionHandler;
import org.jaguar.commons.oauth2.properties.SpringSecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;

/**
 * @author lvws
 * @since 2021/6/23
 */
@Order(1)
@Configuration
@RequiredArgsConstructor
public class ActuatorWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final SpringSecurityProperties springSecurityProperties;

    private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationExceptionHandler authenticationExceptionHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/actuator").antMatcher("/actuator/**")
                .userDetailsService(new ClientDetailsUserDetailsService(clientId -> {
                    OAuth2ResourceServerProperties.Opaquetoken opaquetoken = oAuth2ResourceServerProperties.getOpaquetoken();
                    if (opaquetoken == null || opaquetoken.getClientId() == null || !opaquetoken.getClientId().equals(clientId)) {
                        return null;
                    }

                    BaseClientDetails clientDetails = new BaseClientDetails();
                    clientDetails.setClientId(opaquetoken.getClientId());
                    clientDetails.setClientSecret(passwordEncoder.encode(opaquetoken.getClientSecret()));
                    return clientDetails;
                }))
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().httpBasic().authenticationEntryPoint(authenticationExceptionHandler);
    }

}
