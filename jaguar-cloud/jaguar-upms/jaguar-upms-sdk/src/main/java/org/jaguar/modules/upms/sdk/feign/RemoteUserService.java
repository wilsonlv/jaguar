package org.jaguar.modules.upms.sdk.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author lvws
 * @since 2021/6/29
 */
@FeignClient(value = "jaguar-upms-server", contextId = "remoteUserService")
public interface RemoteUserService extends UserDetailsService {

    /**
     * 根据用户名巡查用户
     *
     * @param username 用户名
     * @return 用户
     */
    @Override
    @GetMapping("/feign/user/loadUserByUsername")
    UserDetails loadUserByUsername(String username);

}
