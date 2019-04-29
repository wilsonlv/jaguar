package com.jaguar.web;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.jaguar.core.util.DateUtil;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by lvws on 2019/4/22.
 */
@Configuration
@PropertySource("classpath:web.properties")
public class SpringMVCConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter(DateUtil.DatePattern.HH_MM_SS.toString()));
        registry.addFormatter(new DateFormatter(DateUtil.DatePattern.YYYY_MM_DD.toString()));
        registry.addFormatter(new DateFormatter(DateUtil.DatePattern.YYYY_MM_DD_HH_MM_SS.toString()));
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder ->
                jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance)
                        .serializerByType(Long.TYPE, ToStringSerializer.instance);
    }

}
