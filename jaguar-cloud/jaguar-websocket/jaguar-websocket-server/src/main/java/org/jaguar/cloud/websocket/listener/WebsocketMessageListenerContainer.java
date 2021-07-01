package org.jaguar.cloud.websocket.listener;

import lombok.extern.slf4j.Slf4j;
import org.jaguar.cloud.websocket.sdk.WebsocketConstant;
import org.jaguar.cloud.websocket.sdk.dto.WebsocketConnection;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author lvws
 * @since 2021/6/22
 */
@Slf4j
@Component
public class WebsocketMessageListenerContainer {

    @JmsListener(destination = WebsocketConstant.DESTINATION_WEBSOCKET_MESSAGE, containerFactory = "topicListener")
    public void listen(WebsocketConnection websocketConnection) {

    }


}
