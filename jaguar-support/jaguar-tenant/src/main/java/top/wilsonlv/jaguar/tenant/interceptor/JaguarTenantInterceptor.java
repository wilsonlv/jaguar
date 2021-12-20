package top.wilsonlv.jaguar.tenant.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.wilsonlv.jaguar.oauth2.model.SecurityUser;
import top.wilsonlv.jaguar.oauth2.util.SecurityUtil;
import top.wilsonlv.jaguar.tenant.component.TenantContextManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lvws
 * @since 2021/8/6
 */
@Slf4j
@Component
public class JaguarTenantInterceptor implements HandlerInterceptor {

    private static final String FEIGN = "/feign";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        SecurityUser currentUser = SecurityUtil.getCurrentUser();
        if (currentUser != null) {
            TenantContextManager.setTenantId(currentUser.getTenantId());
            log.debug("resolve tenantId from currentUser : {}", TenantContextManager.getTenantId());
        } else {
            String tenantId = request.getHeader("tenantId");
            if (request.getRequestURI().startsWith(FEIGN) && StringUtils.hasText(tenantId)) {
                TenantContextManager.setTenantId(Long.valueOf(tenantId));
                log.debug("resolve tenantId from feign request : {}", TenantContextManager.getTenantId());
            } else {
                TenantContextManager.setTenantId(null);
                log.debug("cannot resolve tenantId");
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        TenantContextManager.clear();
    }
}
