package top.wilsonlv.jaguar.cloud.websocket.config;

import top.wilsonlv.jaguar.cloud.websocket.handler.WebsocketOnlineHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author lvws
 * @since 2021/6/22
 */
@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    public static final String SERVER_INSTANCE_ID = UUID.randomUUID().toString();

    @Resource
    private WebsocketOnlineHandler websocketOnlineHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(websocketOnlineHandler, "/websocket")
                // 允许跨域
                .setAllowedOrigins("*");
    }

}
