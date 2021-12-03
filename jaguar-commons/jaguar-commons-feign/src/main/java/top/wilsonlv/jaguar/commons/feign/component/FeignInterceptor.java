package top.wilsonlv.jaguar.commons.feign.component;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityUser;
import top.wilsonlv.jaguar.commons.oauth2.properties.JaguarSecurityProperties;
import top.wilsonlv.jaguar.commons.oauth2.util.SecurityUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author lvws
 * @since 2021/6/29
 */
@Component
@RequiredArgsConstructor
public class FeignInterceptor implements RequestInterceptor {

    private final JaguarSecurityProperties jaguarSecurityProperties;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String basic = Base64Utils.encodeToString((jaguarSecurityProperties.getServerId() + ":" + jaguarSecurityProperties.getServerSecret())
                .getBytes(StandardCharsets.UTF_8));
        requestTemplate.header(HttpHeaders.AUTHORIZATION, "Basic " + basic);

        SecurityUser currentUser = SecurityUtil.getCurrentUser();
        if (currentUser != null && currentUser.getTenantId() != null) {
            requestTemplate.header("tenantId", currentUser.getTenantId().toString());
        }
    }

}
