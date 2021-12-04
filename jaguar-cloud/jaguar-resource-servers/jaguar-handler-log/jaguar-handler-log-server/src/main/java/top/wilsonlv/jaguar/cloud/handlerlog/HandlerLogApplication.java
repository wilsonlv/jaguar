package top.wilsonlv.jaguar.cloud.handlerlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import top.wilsonlv.jaguar.openfeign.annotation.EnableJaguarFeignClients;

/**
 * @author lvws
 * @since 2021/8/6
 */
@EnableJaguarFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = {"top.wilsonlv.jaguar.**.repository"})
public class HandlerLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(HandlerLogApplication.class, args);
    }

}
