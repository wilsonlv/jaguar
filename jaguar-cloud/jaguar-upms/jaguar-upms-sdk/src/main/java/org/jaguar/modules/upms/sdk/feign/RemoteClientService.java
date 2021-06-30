package org.jaguar.modules.upms.sdk.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lvws
 * @since 2021/6/29
 */
@FeignClient(value = "jaguar-upms-server", contextId = "remoteClientService")
public interface RemoteClientService {

    /**
     * 根据clientId查询client
     *
     * @param clientId clientId
     * @return client
     */
    @GetMapping("/feign/client/loadClientByClientId")
    BaseClientDetails loadClientByClientId(@RequestParam("clientId") String clientId);

}
