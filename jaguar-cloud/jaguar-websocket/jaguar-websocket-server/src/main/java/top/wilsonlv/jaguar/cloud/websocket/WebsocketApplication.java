package top.wilsonlv.jaguar.cloud.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import top.wilsonlv.jaguar.commons.feign.annotation.EnableJaguarFeignClients;

/**
 * @author lvws
 * @since 2021/6/22
 */
@EnableJaguarFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class WebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketApplication.class, args);
    }

}
