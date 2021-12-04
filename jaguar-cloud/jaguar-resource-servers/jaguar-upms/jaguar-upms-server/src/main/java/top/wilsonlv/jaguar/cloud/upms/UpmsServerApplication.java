package top.wilsonlv.jaguar.cloud.upms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import top.wilsonlv.jaguar.openfeign.annotation.EnableJaguarFeignClients;
import top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarMapperScan;

/**
 * @author lvws
 * @since 2021/4/6
 */
@JaguarMapperScan
@EnableJaguarFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class UpmsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpmsServerApplication.class, args);
    }

}