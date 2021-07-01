package org.jaguar.cloud.websocket.sdk.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.jaguar.cloud.websocket.sdk.enums.WebsocketMessageType;
import org.jaguar.cloud.websocket.sdk.enums.WebsocketPage;

/**
 * @author lvws
 * @since 2021/4/30
 */
@Data
public class WebsocketMessage {

	private WebsocketMessageType websocketMessageType;

	private WebsocketPage websocketPage;

	private JSONObject data;

	public WebsocketMessage() {
	}

	public WebsocketMessage(WebsocketMessageType websocketMessageType) {
		this.websocketMessageType = websocketMessageType;
	}

	public WebsocketMessage(WebsocketMessageType websocketMessageType, JSONObject data) {
		this.websocketMessageType = websocketMessageType;
		this.data = data;
	}

	public WebsocketMessage(WebsocketMessageType websocketMessageType, WebsocketPage websocketPage) {
		this.websocketMessageType = websocketMessageType;
		this.websocketPage = websocketPage;
	}

	public WebsocketMessage(WebsocketMessageType websocketMessageType, WebsocketPage websocketPage, JSONObject data) {
		this.websocketMessageType = websocketMessageType;
		this.websocketPage = websocketPage;
		this.data = data;
	}

	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}

}
