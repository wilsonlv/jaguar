package top.wilsonlv.jaguar.commons.tenant.component;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import top.wilsonlv.jaguar.commons.tenant.properties.TenantProperties;

/**
 * @author lvws
 * <p>
 * ·
 */
@Component
public class JaguarTenantLineHandler implements TenantLineHandler {

    @Autowired
    private TenantProperties tenantProperties;

    @Override
    public Expression getTenantId() {
        Long tenantId = TenantContextManager.getTenantId();
        if (tenantId == null) {
            return new NullValue();
        } else {
            return new LongValue(tenantId);
        }
    }

    @Override
    public String getTenantIdColumn() {
        return tenantProperties.getTenantIdColumn();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return tenantProperties.getIgnoreTables() != null && tenantProperties.getIgnoreTables().contains(tableName);
    }

}
