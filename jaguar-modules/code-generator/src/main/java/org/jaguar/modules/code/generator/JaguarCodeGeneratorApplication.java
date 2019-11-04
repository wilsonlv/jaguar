package org.jaguar.modules.code.generator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by lvws on 2019/5/23.
 */
@SpringBootApplication
@MapperScan(basePackages = {"org.jaguar.**.mapper"})
@ComponentScan({"org.jaguar.core.base"})
public class JaguarCodeGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(JaguarCodeGeneratorApplication.class);
    }

}
