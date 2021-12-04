package top.wilsonlv.jaguar.cloud.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import top.wilsonlv.jaguar.cloud.auth.component.oauth.ServerTokenService;
import top.wilsonlv.jaguar.cloud.upms.sdk.component.JaguarUserDetailsServiceImpl;
import top.wilsonlv.jaguar.oauth2.component.AuthenticationExceptionHandler;
import top.wilsonlv.jaguar.oauth2.component.RedisClientDetailsServiceImpl;

/**
 * @author lvws
 * @since 2021/4/8
 */
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final ServerTokenService tokenServices;

    private final AuthenticationManager authenticationManager;

    private final JaguarUserDetailsServiceImpl jaguarUserDetailsService;

    private final RedisClientDetailsServiceImpl clientDetailsService;

    private final AuthenticationExceptionHandler authenticationExceptionHandler;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(jaguarUserDetailsService)
                .tokenServices(tokenServices)
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
