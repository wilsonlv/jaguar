package top.wilsonlv.jaguar.cloud.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import top.wilsonlv.jaguar.commons.web.filter.RequestParamsFilterConfig;

/**
 * @author lvws
 * @since 2021/6/22
 */
@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication(exclude = RequestParamsFilterConfig.class)
public class MonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitorApplication.class, args);
    }

}
