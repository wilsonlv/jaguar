package org.jaguar.modules.auth.server;

import org.jaguar.commons.feign.annotation.EnableJaguarFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lvws
 * @since 2021/6/8
 */
@EnableJaguarFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

}