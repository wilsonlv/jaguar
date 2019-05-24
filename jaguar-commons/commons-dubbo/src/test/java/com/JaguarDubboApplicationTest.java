package com;

import org.apache.dubbo.config.annotation.Reference;
import org.jaguar.commons.dubbo.provider.IBaseProvider;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by lvws on 2019/4/18.
 */
@SpringBootApplication
public class JaguarDubboApplicationTest {

    @Reference(version = "1.0.0", url = "dubbo://127.0.0.1:18080", check = false)
    private IBaseProvider provider;

    public static void main(String[] args) {
        SpringApplication.run(JaguarDubboApplicationTest.class, args);
    }

    @Bean
    public ApplicationRunner runner() {
        return args -> {

        };
    }

}
