package org.jaguar.modules.code.generator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author lvws
 * @since 2019/5/23.
 */
@SpringBootApplication
@MapperScan(basePackages = {"org.jaguar.**.mapper"})
@ComponentScan({"org.jaguar.core", "org.jaguar.commons","org.jaguar.modules"})
@ServletComponentScan({"org.jaguar"})
public class JaguarCodeGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(JaguarCodeGeneratorApplication.class);
    }

}
