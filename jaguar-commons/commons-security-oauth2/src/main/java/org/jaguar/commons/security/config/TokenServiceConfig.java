package org.jaguar.commons.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * @author lvws
 * @since 2021/6/8
 */
@Configuration
public class TokenServiceConfig {

    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;
    @Autowired
    private AuthorizationServerProperties authorizationServerProperties;

    @Bean
    public ResourceServerTokenServices tokenServices(){
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setClientId(oAuth2ClientProperties.getClientId());
        tokenServices.setClientSecret(oAuth2ClientProperties.getClientSecret());
        tokenServices.setCheckTokenEndpointUrl(authorizationServerProperties.getCheckTokenAccess());
        return tokenServices;
    }

}
