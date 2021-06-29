package org.jaguar.modules.auth.sdk.feign;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lvws
 * @since 2021/6/29
 */
@FeignClient(value = "jaguar-auth-server", contextId = "remoteAuthService")
public interface RemoteAuthService {

}
