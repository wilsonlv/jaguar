package org.jaguar.modules.workflow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by lvws on 2019/4/18.
 */
@SpringBootApplication
@MapperScan(basePackages = {"org.jaguar.**.mapper"})
@ComponentScan({"org.jaguar.core.base"})
public class JaguarWorkflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(JaguarWorkflowApplication.class, args);
    }

}
