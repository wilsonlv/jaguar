package org.jaguar.modules.websocket.config;

import lombok.Getter;
import org.jaguar.modules.websocket.handler.WebsocketOnlineHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.UUID;

/**
 * @author lvws
 * @since 2021/6/22
 */
@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    public static final String SERVER_INSTANCE_ID = UUID.randomUUID().toString();

    @Autowired
    private WebsocketOnlineHandler websocketOnlineHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(websocketOnlineHandler, "/websocket")
                // 允许跨域
                .setAllowedOrigins("*");
    }

}
