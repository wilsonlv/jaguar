package top.wilsonlv.jaguar.cloud.websocket.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import top.wilsonlv.jaguar.oauth2.model.SecurityUser;
import top.wilsonlv.jaguar.cloud.websocket.config.WebsocketConfig;
import top.wilsonlv.jaguar.cloud.websocket.sdk.dto.WebsocketConnection;
import top.wilsonlv.jaguar.cloud.websocket.sdk.enums.WebsocketPage;
import top.wilsonlv.jaguar.cloud.websocket.wrapper.WebsocketSessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lvws
 * @since 2021/4/30
 */
@Slf4j
public abstract class BaseOnlineHandler extends AbstractWebSocketHandler {

    @Autowired
    protected RedisTemplate<String, Serializable> redisTemplate;

    /**
     * 获取sessionId=>session映射结合
     *
     * @return sessionId=>session映射结合
     */
    abstract Map<String, WebsocketSessionWrapper> getWsSessionIdMapping();


    public WebsocketSessionWrapper getSessionWrapper(String sessionId) {
        return getWsSessionIdMapping().get(sessionId);
    }

    public SecurityUser getCurrentUser(WebSocketSession session) {
        OAuth2Authentication principal = (OAuth2Authentication) session.getPrincipal();
        return principal == null ? null : (SecurityUser) principal.getPrincipal();
    }

    protected WebsocketSessionWrapper createSessionWrapper(String onlineKey, WebSocketSession session, WebsocketPage websocketPage) {
        WebsocketSessionWrapper websocketSessionWrapper = new WebsocketSessionWrapper(session, websocketPage, onlineKey);
        getWsSessionIdMapping().put(session.getId(), websocketSessionWrapper);
        return websocketSessionWrapper;
    }

    protected void createConnection(WebsocketSessionWrapper websocketSessionWrapper, JSONObject data) {
        long heartbeat = websocketSessionWrapper.getWebsocketPage().getExpire();

        WebsocketConnection websocketConnection = new WebsocketConnection();
        websocketConnection.setWsSessionId(websocketSessionWrapper.getWebSocketSession().getId());
        websocketConnection.setServerInstanceId(WebsocketConfig.SERVER_INSTANCE_ID);
        websocketConnection.setExpireTime(LocalDateTime.now().plusSeconds(heartbeat));
        websocketConnection.setWebsocketPage(websocketSessionWrapper.getWebsocketPage());
        websocketConnection.setConnectionParams(data);

        BoundHashOperations<String, Object, Object> operations = this.redisTemplate.boundHashOps(websocketSessionWrapper.getOnlineKey());
        operations.put(websocketSessionWrapper.getWebSocketSession().getId(), websocketConnection);
        operations.expire(heartbeat, TimeUnit.SECONDS);
    }


    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        if (log.isDebugEnabled()) {
            log.debug("establish websocket session : {}", session.getId());
        }
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) {
        log.debug(exception.getMessage(), exception);
        this.afterConnectionClosed(session, null);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @Nullable CloseStatus status) {
        WebsocketSessionWrapper sessionWrapper = getWsSessionIdMapping().get(session.getId());
        if (sessionWrapper == null) {
            return;
        }

        SecurityUser currentUser = getCurrentUser(session);

        this.redisTemplate.boundHashOps(sessionWrapper.getOnlineKey()).delete(session.getId());
        getWsSessionIdMapping().remove(session.getId());

        if (log.isDebugEnabled()) {
            log.debug("close websocket session : {}, username : {}, status : {}", session.getId(),
                    currentUser.getUsername(), status != null ? status.getCode() : "error");
        }
    }

}
