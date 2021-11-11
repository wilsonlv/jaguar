package top.wilsonlv.jaguar.cloud.auth.sdk.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.wilsonlv.jaguar.cloud.services.JaguarServerName;
import top.wilsonlv.jaguar.cloud.services.JaguarServiceName;

/**
 * @author lvws
 * @since 2021/6/29
 */
@FeignClient(value = JaguarServerName.JAGUAR_AUTH_SERVER, contextId = JaguarServiceName.REMOTE_AUTH_SERVICE)
public interface RemoteAuthService {

    /**
     * 获取oauth2 token
     *
     * @param username  用户名
     * @param password  密码
     * @param grantType 授权类型（“password”）
     * @return token
     */
    @PostMapping("/oauth/token")
    DefaultOAuth2AccessToken oauthToken(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("grant_type") String grantType);

}
