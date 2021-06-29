package org.jaguar.modules.upms.sdk.feign;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lvws
 * @since 2021/6/29
 */
@ConditionalOnMissingClass("org.jaguar.modules.upms.server.service.UpmsClientDetailsServiceImpl")
@FeignClient(value = "jaguar-upms-server", contextId = "remoteClientService")
public interface RemoteClientService extends ClientDetailsService {

    /**
     * 根据clientId查询client
     *
     * @param clientId clientId
     * @return client
     */
    @Override
    @GetMapping("/feign/client/loadClientByClientId")
    ClientDetails loadClientByClientId(@RequestParam("clientId") String clientId);

}
