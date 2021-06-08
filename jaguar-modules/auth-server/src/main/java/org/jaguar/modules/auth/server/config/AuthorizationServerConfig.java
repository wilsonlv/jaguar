package org.jaguar.modules.auth.server.config;

import org.jaguar.commons.security.component.AuthenticationExceptionHandler;
import org.jaguar.modules.auth.server.component.JaguarClientDetailsServiceImpl;
import org.jaguar.modules.auth.server.component.JaguarUserDetailsServiceImpl;
import org.jaguar.modules.auth.server.component.OauthTokenExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author lvws
 * @since 2021/4/8
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private JaguarUserDetailsServiceImpl userDetailService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JaguarClientDetailsServiceImpl clientDetailsService;

    @Autowired
    private AuthenticationExceptionHandler authenticationExceptionHandler;

    @Autowired
    private OauthTokenExceptionTranslator oauthTokenExceptionTranslator;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailService)
                .tokenStore(tokenStore)
                .exceptionTranslator(oauthTokenExceptionTranslator);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.allowFormAuthenticationForClients()
                .authenticationEntryPoint(authenticationExceptionHandler)
                .checkTokenAccess("isAuthenticated()");
    }

}
