package top.wilsonlv.jaguar.cloud.websocket.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityUser;
import top.wilsonlv.jaguar.cloud.websocket.sdk.WebsocketConstant;
import top.wilsonlv.jaguar.cloud.websocket.sdk.dto.WebsocketMessage;
import top.wilsonlv.jaguar.cloud.websocket.sdk.enums.WebsocketPage;
import top.wilsonlv.jaguar.cloud.websocket.sdk.pusher.WebsocketPusher;
import top.wilsonlv.jaguar.cloud.websocket.wrapper.WebsocketSessionWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lvws
 * @since 2021/6/22
 */
@Slf4j
@Component
public class WebsocketOnlineHandler extends BaseOnlineHandler {

    private static final Map<String, WebsocketSessionWrapper> WS_SESSION_ID_MAPPING = new ConcurrentHashMap<>();

    @Resource
    private WebsocketPusher websocketPusher;

    @Override
    Map<String, WebsocketSessionWrapper> getWsSessionIdMapping() {
        return WS_SESSION_ID_MAPPING;
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws IOException {
        log.debug("收到消息:{}", message.getPayload());
        SecurityUser currentUser = getCurrentUser(session);

        WebsocketMessage websocketMessage = JSONObject.parseObject(message.getPayload(), WebsocketMessage.class);
        if (websocketMessage.getWebsocketPage() == null) {
            return;
        }

        JSONObject data = websocketMessage.getData();
        switch (websocketMessage.getWebsocketMessageType()) {
            case PING: {
                this.pingHandler(session, websocketMessage.getWebsocketPage(), data);
                break;
            }
            default:
        }
    }

    private void pingHandler(WebSocketSession session, WebsocketPage websocketPage, JSONObject data) throws IOException {
        switch (websocketPage) {
            case ADMIN_INDEX:
                break;
            default:
                return;
        }

        String onlineKey = String.format(WebsocketConstant.ONLINE_KEY_FORMAT, getCurrentUser(session).getId());

        WebsocketSessionWrapper websocketSessionWrapper = super.createSessionWrapper(onlineKey, session, websocketPage);
        super.createConnection(websocketSessionWrapper, data);
        websocketSessionWrapper.pong();
    }

}
