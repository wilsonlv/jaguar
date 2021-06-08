package org.jaguar.modules.auth.server.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/6/7
 */
@Component
public class JaguarClientDetailsServiceImpl implements ClientDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Set<String> authorizedGrantTypes = new HashSet<>();
        authorizedGrantTypes.add("refresh_token");
        authorizedGrantTypes.add("authorization_code");
        authorizedGrantTypes.add("password");

        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientId("client1");
        clientDetails.setClientSecret(passwordEncoder.encode("client1"));
        clientDetails.setAuthorizedGrantTypes(authorizedGrantTypes);
        clientDetails.setRegisteredRedirectUri(Collections.singleton("http://localhost:7777/index"));
        clientDetails.setAccessTokenValiditySeconds(3600);
        clientDetails.setScope(Collections.singleton("all"));

        return clientDetails;
    }
}
