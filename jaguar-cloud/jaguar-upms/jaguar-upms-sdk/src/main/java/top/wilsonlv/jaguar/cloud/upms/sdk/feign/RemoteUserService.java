package top.wilsonlv.jaguar.cloud.upms.sdk.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.wilsonlv.jaguar.commons.enums.UserType;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityUser;

/**
 * @author lvws
 * @since 2021/6/29
 */
@FeignClient(value = "jaguar-upms-server", contextId = "remoteUserService")
public interface RemoteUserService {

    /**
     * 根据用户名巡查用户
     *
     * @param username 用户名
     * @return 用户
     */
    @GetMapping("/feign/user/loadUserByUsername")
    SecurityUser loadUserByUsername(@RequestParam("username") String username, @RequestParam("userType") UserType userType);

}
