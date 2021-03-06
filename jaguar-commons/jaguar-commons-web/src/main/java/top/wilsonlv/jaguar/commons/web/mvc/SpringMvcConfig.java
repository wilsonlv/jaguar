package top.wilsonlv.jaguar.commons.web.mvc;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lvws
 * @since 2019/4/22.
 */
@CrossOrigin
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN);

    /**
     * Jackson εΊεε
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder ->
                builder
                        .serializerByType(Long.class, Long2StringSerializer.INSTANCE)
                        .serializerByType(Long.TYPE, Long2StringSerializer.INSTANCE)
                        .serializerByType(LocalTime.class, new LocalTimeSerializer(TIME_FORMATTER))
                        .serializerByType(LocalDate.class, new LocalDateSerializer(DATE_FORMATTER))
                        .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER))
                        .deserializerByType(LocalTime.class, new LocalDateTimeDeserializer(TIME_FORMATTER))
                        .deserializerByType(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER))
                        .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER));
    }

    /**
     * ε’ε GETθ―·ζ±εζ°δΈ­ζΆι΄η±»εθ½¬ζ’
     * <ul>
     * <li>HH:mm:ss -> LocalTime</li>
     * <li>yyyy-MM-dd -> LocalDate</li>
     * <li>yyyy-MM-dd HH:mm:ss -> LocalDateTime</li>
     * </ul>
     */
    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setTimeFormatter(TIME_FORMATTER);
        registrar.setDateFormatter(DATE_FORMATTER);
        registrar.setDateTimeFormatter(DATE_TIME_FORMATTER);
        registrar.registerFormatters(registry);
    }

    /**
     * θ§£ε³θ·¨ε
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
    }

}
