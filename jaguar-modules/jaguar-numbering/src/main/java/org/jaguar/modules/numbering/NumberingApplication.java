package org.jaguar.modules.numbering;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author lvws
 * @since 2021/4/8
 */
@MapperScan(basePackages = "org.jaguar.modules.numbering")
@ServletComponentScan("org.jaguar")
@SpringBootApplication
public class NumberingApplication {

    public static void main(String[] args) {
        SpringApplication.run(NumberingApplication.class, args);
    }

}
