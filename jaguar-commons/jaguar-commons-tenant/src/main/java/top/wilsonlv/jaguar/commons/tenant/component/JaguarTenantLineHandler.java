package top.wilsonlv.jaguar.commons.tenant.component;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityUser;
import top.wilsonlv.jaguar.commons.oauth2.util.SecurityUtil;
import top.wilsonlv.jaguar.commons.tenant.properties.TenantProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        SecurityUser currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null || currentUser.getTenantId() == null) {
            return new NullValue();
        }

        return new LongValue(currentUser.getTenantId());
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
