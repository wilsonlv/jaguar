package org.jaguar.modules.numbering;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author lvws
 * @since 2021/4/8
 */
@MapperScan(basePackages = "org.jaguar.modules.numbering")
@ServletComponentScan("org.jaguar")
@EnableRedisHttpSession
@SpringBootApplication
public class NumberingApplication {

    public static void main(String[] args) {
        SpringApplication.run(NumberingApplication.class, args);
    }

}
