package org.jaguar.modules.system.mgm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author lvws
 * @since 2021/4/6
 */
@ComponentScan({"org.jaguar.commons.basecrud", "org.jaguar.modules.system.mgm"})
@MapperScan(basePackages = "org.jaguar.modules.system.mgm.mapper")
@ServletComponentScan("org.jaguar")
@EnableRedisHttpSession
@SpringBootApplication
public class SystemMgmServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemMgmServerApplication.class, args);
    }

}
