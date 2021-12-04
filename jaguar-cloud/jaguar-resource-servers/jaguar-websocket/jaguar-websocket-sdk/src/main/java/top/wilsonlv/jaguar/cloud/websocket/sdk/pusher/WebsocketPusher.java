package top.wilsonlv.jaguar.cloud.websocket.sdk.pusher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import top.wilsonlv.jaguar.cloud.websocket.sdk.WebsocketConstant;
import top.wilsonlv.jaguar.cloud.websocket.sdk.dto.WebsocketConnection;
import top.wilsonlv.jaguar.cloud.websocket.sdk.dto.WebsocketMessage;
import top.wilsonlv.jaguar.cloud.websocket.sdk.enums.WebsocketPage;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author lvws
 * @since 2021/5/6
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebsocketPusher {

    protected final RedisTemplate<String, Serializable> redisTemplate;

    protected final JmsTemplate jmsTopicTemplate;


    public void push2OnlineKey(String onlineKey, WebsocketMessage message, WebsocketPage websocketPage) {
        Map<Object, Object> entries = redisTemplate.boundHashOps(onlineKey).entries();
        if (entries == null) {
            return;
        }

        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            WebsocketConnection websocketConnection = (WebsocketConnection) entry.getValue();
            if (websocketConnection.getExpireTime().isBefore(LocalDateTime.now())) {
                continue;
            }
            if (websocketPage != null && websocketPage != websocketConnection.getWebsocketPage()) {
                continue;
            }

            jmsTopicTemplate.send(WebsocketConstant.DESTINATION_WEBSOCKET_MESSAGE, session -> session.createObjectMessage(websocketConnection));
        }
    }

}