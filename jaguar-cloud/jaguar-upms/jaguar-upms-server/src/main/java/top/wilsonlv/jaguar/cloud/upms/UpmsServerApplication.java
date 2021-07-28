package top.wilsonlv.jaguar.cloud.upms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import top.wilsonlv.jaguar.commons.feign.annotation.EnableJaguarFeignClients;

/**
 * @author lvws
 * @since 2021/4/6
 */
@EnableJaguarFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = {"top.wilsonlv.jaguar.**.repository"})
public class UpmsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpmsServerApplication.class, args);
    }

}