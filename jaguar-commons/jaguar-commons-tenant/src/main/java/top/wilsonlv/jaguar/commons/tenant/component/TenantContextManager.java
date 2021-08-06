package top.wilsonlv.jaguar.commons.tenant.component;

import org.springframework.core.NamedThreadLocal;

/**
 * @author lvws
 * @since 2021/8/6
 */
public class TenantContextManager {

    public static final ThreadLocal<Long> TENANT_ID = new NamedThreadLocal<>("TenantId");

    public static void setTenantId(Long tenantId) {
        TENANT_ID.set(tenantId);
    }

    public static Long getTenantId() {
        return TENANT_ID.get();
    }

    public static void clear() {
        TENANT_ID.remove();
    }

}
