package org.jaguar.commons.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author lvws
 * @since 2020/5/30
 */
@Configuration
public class WsConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public WsSpringConfigurator wsSpringConfigurator() {
        return new WsSpringConfigurator();
    }
}
