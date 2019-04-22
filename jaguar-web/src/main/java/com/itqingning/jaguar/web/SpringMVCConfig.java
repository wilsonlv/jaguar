package com.itqingning.jaguar.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by lvws on 2019/4/22.
 */
@Configuration
@PropertySource("classpath:web.properties")
public class SpringMVCConfig implements WebMvcConfigurer {



}
