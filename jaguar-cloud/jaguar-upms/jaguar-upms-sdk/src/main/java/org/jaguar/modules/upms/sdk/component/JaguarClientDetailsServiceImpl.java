package org.jaguar.modules.upms.sdk.component;

import lombok.RequiredArgsConstructor;
import org.jaguar.modules.upms.sdk.feign.RemoteClientService;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Component;

/**
 * @author lvws
 * @since 2021/6/30
 */
@Component
@RequiredArgsConstructor
public class JaguarClientDetailsServiceImpl implements ClientDetailsService {

    private final RemoteClientService remoteClientService;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return remoteClientService.loadClientByClientId(clientId);
    }
}
