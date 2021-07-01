package org.jaguar.cloud.upms.server;

import org.jaguar.commons.feign.annotation.EnableJaguarFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lvws
 * @since 2021/4/6
 */
@EnableJaguarFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class UpmsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpmsServerApplication.class, args);
    }

}