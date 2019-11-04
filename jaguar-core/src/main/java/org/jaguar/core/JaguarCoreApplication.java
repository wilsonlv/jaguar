package org.jaguar.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by lvws on 2019/5/23.
 */
@SpringBootApplication
@MapperScan(basePackages = {"org.jaguar.**.mapper"})
public class JaguarCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(JaguarCoreApplication.class);
    }

}
