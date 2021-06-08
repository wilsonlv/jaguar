package org.jaguar.support.tenant.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;

/**
 * @author lvws
 * <p>
 * ·
 */
public class JaguarTenantLineHandler implements TenantLineHandler {

    @Override
    public Expression getTenantId() {
        return null;
    }

    @Override
    public String getTenantIdColumn() {
        return null;
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return false;
    }
}
