package org.jaguar;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lvws
 * @since 2019/5/23.
 */
@SpringBootApplication
@MapperScan(basePackages = {"org.jaguar.**.mapper"})
public class JaguarCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(JaguarCoreApplication.class);
    }

}
