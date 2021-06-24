package org.jaguar.support.tenant.component;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import org.jaguar.commons.oauth2.model.SecurityUser;
import org.jaguar.commons.oauth2.util.SecurityUtil;
import org.springframework.stereotype.Component;

/**
 * @author lvws
 * <p>
 * ·
 */
@Component
public class JaguarTenantLineHandler implements TenantLineHandler {

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
        return "tenant_id";
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return false;
    }
}
