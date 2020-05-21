package org.jaguar.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author lvws
 * @since 2019/4/17.
 */
@SpringBootApplication
@MapperScan(basePackages = {"org.jaguar.**.mapper"})
@ComponentScan({"org.jaguar"})
@ServletComponentScan({"org.jaguar"})
public class JaguarApplicationTest {

    public static void main(String[] args) {
        SpringApplication.run(JaguarApplicationTest.class, args);
    }

}
