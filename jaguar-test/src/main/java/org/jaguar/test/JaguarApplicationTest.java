package org.jaguar.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by lvws on 2019/4/17.
 */
@SpringBootApplication
@MapperScan(basePackages = {"org.jaguar.**.mapper"})
@ComponentScan({"org.jaguar.core", "org.jaguar.commons", "org.jaguar.modules"})
public class JaguarApplicationTest {

    public static void main(String[] args) {
        SpringApplication.run(JaguarApplicationTest.class, args);
    }

    @Bean
    public ApplicationRunner runner() {
        return args -> {

        };
    }

}
