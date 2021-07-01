package org.jaguar.cloud.auth.server.config;

import lombok.RequiredArgsConstructor;
import org.jaguar.cloud.auth.server.component.OauthTokenExceptionTranslator;
import org.jaguar.commons.oauth2.component.AuthenticationExceptionHandler;
import org.jaguar.commons.oauth2.component.RedisClientDetailsServiceImpl;
import org.jaguar.cloud.auth.server.component.AuthServerTokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**
 * @author lvws
 * @since 2021/4/8
 */
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthServerTokenService tokenServices;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailService;

    private final RedisClientDetailsServiceImpl clientDetailsService;

    private final AuthenticationExceptionHandler authenticationExceptionHandler;

    private final OauthTokenExceptionTranslator oauthTokenExceptionTranslator;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailService)
                .tokenServices(tokenServices)
                .exceptionTranslator(oauthTokenExceptionTranslator);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.allowFormAuthenticationForClients()
                .authenticationEntryPoint(authenticationExceptionHandler)
                .checkTokenAccess("isAuthenticated()");
    }

}
