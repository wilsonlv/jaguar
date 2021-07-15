package org.jaguar.cloud.auth.server;

import org.jaguar.commons.feign.annotation.EnableJaguarFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author lvws
 * @since 2021/6/8
 */
@EnableJaguarFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = {"org.jaguar.**.repository"})
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

}