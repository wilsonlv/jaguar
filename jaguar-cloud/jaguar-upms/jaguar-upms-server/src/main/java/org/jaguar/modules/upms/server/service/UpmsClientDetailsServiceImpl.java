package org.jaguar.modules.upms.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/6/29
 */
@Primary
@Service
@RequiredArgsConstructor
public class UpmsClientDetailsServiceImpl implements ClientDetailsService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public ClientDetails loadClientByClientId(String clientId) {
        Set<String> authorizedGrantTypes = new HashSet<>();
        authorizedGrantTypes.add("refresh_token");
        authorizedGrantTypes.add("authorization_code");
        authorizedGrantTypes.add("password");

        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientId(clientId);
        clientDetails.setClientSecret(passwordEncoder.encode("123456"));
        clientDetails.setAuthorizedGrantTypes(authorizedGrantTypes);
        clientDetails.setRegisteredRedirectUri(Collections.singleton("http://localhost:7777/index"));
        clientDetails.setAccessTokenValiditySeconds(3600);
        clientDetails.setScope(Collections.singleton("all"));
        return clientDetails;
    }
}
