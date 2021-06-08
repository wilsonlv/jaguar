package org.jaguar.modules.demo.server.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lvws
 * @since 2021/6/8
 */
@RestController
@RequestMapping("/demo")
public class DemoApi {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

}
