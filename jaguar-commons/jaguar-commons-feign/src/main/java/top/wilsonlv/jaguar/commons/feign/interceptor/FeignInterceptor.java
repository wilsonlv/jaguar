package top.wilsonlv.jaguar.commons.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityUser;
import top.wilsonlv.jaguar.commons.oauth2.util.SecurityUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author lvws
 * @since 2021/6/29
 */
@Component
public class FeignInterceptor implements RequestInterceptor {

    @Autowired
    private OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        OAuth2ResourceServerProperties.Opaquetoken opaqueToken = oAuth2ResourceServerProperties.getOpaquetoken();
        String basic = Base64Utils.encodeToString((opaqueToken.getClientId() + ":" + opaqueToken.getClientSecret()).getBytes(StandardCharsets.UTF_8));
        requestTemplate.header(HttpHeaders.AUTHORIZATION, "Basic " + basic);

        SecurityUser currentUser = SecurityUtil.getCurrentUser();
        if (currentUser != null && currentUser.getTenantId() != null) {
            requestTemplate.header("tenantId", currentUser.getTenantId().toString());
        }
    }

}
