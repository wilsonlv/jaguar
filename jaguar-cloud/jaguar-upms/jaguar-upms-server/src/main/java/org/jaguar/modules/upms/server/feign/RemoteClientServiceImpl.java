package org.jaguar.modules.upms.server.feign;

import lombok.RequiredArgsConstructor;
import org.jaguar.modules.upms.sdk.feign.RemoteClientService;
import org.jaguar.modules.upms.server.service.UpmsClientDetailsServiceImpl;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * @author lvws
 * @since 2021/6/29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/feign/client")
public class RemoteClientServiceImpl implements RemoteClientService {

    private final UpmsClientDetailsServiceImpl clientDetailsService;

    @Override
    @GetMapping("/loadClientByClientId")
    public ClientDetails loadClientByClientId(@RequestParam @NotBlank String clientId) {
        return clientDetailsService.loadClientByClientId(clientId);
    }
}
