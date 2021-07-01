package org.jaguar.cloud.websocket.sdk.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.jaguar.cloud.websocket.sdk.enums.WebsocketPage;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lvws
 * @since 2021/4/21
 */
@Data
public class WebsocketConnection implements Serializable {

    private String wsSessionId;

    private LocalDateTime expireTime;

    private String serverInstanceId;

    private WebsocketPage websocketPage;

    private JSONObject connectionParams;

}
