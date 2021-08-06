package top.wilsonlv.jaguar.cloud.handlerlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import top.wilsonlv.jaguar.commons.feign.annotation.EnableJaguarFeignClients;

/**
 * @author lvws
 * @since 2021/8/6
 */
@EnableJaguarFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class HandlerLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(HandlerLogApplication.class, args);
    }

}
