package org.jaguar.commons.swagger.config;

import org.jaguar.commons.swagger.properties.SwaggerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lws
 * @since 2019/4/22.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private SwaggerProperties swaggerProperties;
    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public Docket platformApi() {
        List<RequestParameter> parameters = new ArrayList<>();
        RequestParameter build = new RequestParameterBuilder().name("Authorization")
                .in(ParameterType.HEADER).required(false).build();
        parameters.add(build);

        return new Docket(DocumentationType.SWAGGER_2).groupName(applicationName).apiInfo(apiInfo())
                .globalRequestParameters(parameters)
                .forCodeGeneration(true);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(swaggerProperties.getTitle()).description(swaggerProperties.getDescription())
                .contact(new Contact(swaggerProperties.getContactName(), swaggerProperties.getContactUrl(), swaggerProperties.getContactEmail()))
                .version(swaggerProperties.getVersion()).build();
    }

}