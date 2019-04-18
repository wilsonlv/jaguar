package com.itqingning.jaguar.dubbo;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by lvws on 2019/4/18.
 */
@Configuration
@PropertySource("classpath:dubbo.properties")
@ImportResource("classpath:spring/dubbo.xml")
public class DubboConfig {
}
