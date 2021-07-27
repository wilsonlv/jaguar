package top.wilsonlv.jaguar.cloud.auth.config;

import lombok.RequiredArgsConstructor;
import top.wilsonlv.jaguar.cloud.auth.component.oauth.AuthServerTokenService;
import top.wilsonlv.jaguar.cloud.auth.component.oauth.exception.OauthTokenExceptionTranslator;
import top.wilsonlv.jaguar.commons.oauth2.component.AuthenticationExceptionHandler;
import top.wilsonlv.jaguar.commons.oauth2.component.RedisClientDetailsServiceImpl;
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
                .exceptionTranslator(oauthTokenExceptionTranslator)
                .pathMapping("/oauth/error", "/oauthRedirectPage/error")
                .pathMapping("/oauth/confirm_access", "/oauthRedirectPage/confirm_access");
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.allowFormAuthenticationForClients()
                .authenticationEntryPoint(authenticationExceptionHandler)
                .checkTokenAccess("isAuthenticated()");
    }

}
