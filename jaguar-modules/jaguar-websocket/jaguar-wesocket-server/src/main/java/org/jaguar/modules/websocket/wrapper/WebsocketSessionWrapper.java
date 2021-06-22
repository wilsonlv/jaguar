package org.jaguar.modules.websocket.wrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jaguar.modules.websocket.sdk.dto.WebsocketMessage;
import org.jaguar.modules.websocket.sdk.enums.WebsocketMessageType;
import org.jaguar.modules.websocket.sdk.enums.WebsocketPage;
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
