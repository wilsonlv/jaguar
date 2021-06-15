package org.jaguar.modules.upms.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author lvws
 * @since 2021/4/6
 */
@MapperScan(basePackages = "org.jaguar.modules.system.mgm.mapper")
@ServletComponentScan("org.jaguar")
@SpringBootApplication
public class UpmsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpmsServerApplication.class, args);
    }

}
