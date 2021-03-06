package top.wilsonlv.jaguar.codegen.datasource;

import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import top.wilsonlv.jaguar.codegen.service.DataSourceService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lvws
 * @since 2021/6/17
 */
public class JdbcDataSourceProvider extends AbstractJdbcDataSourceProvider {

    private final String driverClassName;

    public JdbcDataSourceProvider(String driverClassName, String url, String username, String password) {
        super(driverClassName, url, username, password);
        this.driverClassName = driverClassName;
    }

    @Override
    protected Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery("select * from jaguar_codegen_datasource where deleted_ = 0");
        Map<String, DataSourceProperty> dynamicDataSources = new HashMap<>();

        while (resultSet.next()) {
            String name = resultSet.getString("name_");
            String host = resultSet.getString("host_");
            String port = resultSet.getString("port_");
            String schema = resultSet.getString("schema_");
            String username = resultSet.getString("username_");
            String password = resultSet.getString("password_");

            DataSourceProperty dataSourceProperty = new DataSourceProperty();
            dataSourceProperty.setUsername(username);
            dataSourceProperty.setPassword(password);
            dataSourceProperty.setUrl(DataSourceService.getJdbcUrl(host, port, schema));
            dataSourceProperty.setDriverClassName(driverClassName);
            dynamicDataSources.put(name, dataSourceProperty);
        }
        return dynamicDataSources;
    }

}
