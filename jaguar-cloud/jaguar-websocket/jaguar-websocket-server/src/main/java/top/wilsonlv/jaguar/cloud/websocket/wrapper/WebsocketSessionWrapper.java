package top.wilsonlv.jaguar.cloud.websocket.wrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.wilsonlv.jaguar.cloud.websocket.sdk.dto.WebsocketMessage;
import top.wilsonlv.jaguar.cloud.websocket.sdk.enums.WebsocketMessageType;
import top.wilsonlv.jaguar.cloud.websocket.sdk.enums.WebsocketPage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @author lvws
 * @since 2021/4/30
 */
@Slf4j
@RequiredArgsConstructor
public class WebsocketSessionWrapper {

    @Getter
    private final WebSocketSession webSocketSession;

    @Getter
    private final WebsocketPage websocketPage;

    @Getter
    private final String onlineKey;

    public void sendMessage(String message) throws IOException {
        synchronized (this.webSocketSession) {
            this.webSocketSession.sendMessage(new TextMessage(message));
        }
    }

    public void pong() throws IOException {
        this.sendMessage(new WebsocketMessage(WebsocketMessageType.PONG).toJsonString());
    }

}
