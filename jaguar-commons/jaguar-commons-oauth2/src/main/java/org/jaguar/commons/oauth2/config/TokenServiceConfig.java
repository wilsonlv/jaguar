package org.jaguar.commons.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author lvws
 * @since 2021/6/8
 */
@Configuration
public class TokenServiceConfig {

    @Autowired
    private TokenStore tokenStore;

    @Bean
    public ResourceServerTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        return defaultTokenServices;
    }

}
