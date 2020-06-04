package org.jaguar.test;

import org.apache.dubbo.config.annotation.Reference;
import org.jaguar.support.dubbo.base.IBaseProvider;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by lvws on 2019/4/18.
 */
@SpringBootApplication
public class JaguarDubboSupoortApplicationTest {

    @Reference(version = "1.0.0", check = false)
    private IBaseProvider provider;

    public static void main(String[] args) {
        SpringApplication.run(JaguarDubboSupoortApplicationTest.class, args);
    }

    @Bean
    public ApplicationRunner runner() {
        return args -> {

        };
    }

}
