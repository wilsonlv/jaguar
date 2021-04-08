package org.jaguar.test;

//import org.jaguar.commons.springsecurity.tokenauth.TokenFactory;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author lvws
 * @since 2019/4/17.
 */
//@EnableRedisHttpSession
@SpringBootApplication
//@MapperScan(basePackages = {"org.jaguar.**.mapper"})
//@ComponentScan({"org.jaguar"})
@ServletComponentScan({"org.jaguar"})
public class JaguarApplicationTest implements ApplicationRunner {

//    @Autowired
//    private TokenFactory tokenFactory;

    public static void main(String[] args) {
        SpringApplication.run(JaguarApplicationTest.class, args);
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {

    }
}
