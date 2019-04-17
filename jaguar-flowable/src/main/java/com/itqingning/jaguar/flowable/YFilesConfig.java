package com.itqingning.jaguar.flowable;

import com.yworks.yfiles.server.graphml.servlets.ExportServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lvws on 2019/3/15.
 */
@Configuration
public class YFilesConfig {

    @Bean
    public ServletRegistrationBean<ExportServlet> ExportServlet() {
        return new ServletRegistrationBean<>(new ExportServlet(), "/yfilesImageExport");
    }

}
