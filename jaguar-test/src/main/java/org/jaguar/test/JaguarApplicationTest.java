package org.jaguar.test;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by lvws on 2019/4/17.
 */
@SpringBootApplication
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
