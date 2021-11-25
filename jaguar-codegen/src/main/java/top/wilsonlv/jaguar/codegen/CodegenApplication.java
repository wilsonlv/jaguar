package top.wilsonlv.jaguar.codegen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.wilsonlv.jaguar.commons.web.exception.JaguarErrorController;

/**
 * @author lvws
 * @since 2021/6/16
 */
@SpringBootApplication(exclude = JaguarErrorController.class)
public class CodegenApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodegenApplication.class, args);
    }

}
