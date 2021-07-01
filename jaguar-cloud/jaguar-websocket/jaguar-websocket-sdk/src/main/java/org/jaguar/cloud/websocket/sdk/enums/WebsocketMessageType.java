package org.jaguar.cloud.websocket.sdk.enums;

/**
 * @author lvws
 * @since 2021/4/30
 */
public enum WebsocketMessageType {

	/**
	 * 心跳
	 */
	PING,
	/**
	 * 心跳
	 */
	PONG,

	/**
	 * 异地登录
	 */
	REMOTE_LOGIN,

	/**
	 * 打开推流
	 */
	OPEN_LIVE_PUSHER,

	/**
	 * 关闭推流
	 */
	CLOSE_PUSH,

	/**
	 * webrtc 提供offer
	 */
	CREATE_OFFER,

	/**
	 * webrtc 提供answer
	 */
	CREATE_ANSWER,

	/**
	 * webrtc onIceCandidate
	 */
	ON_ICE_CANDIDATE,

	/**
	 * 巡考消息
	 */
	INSPECTION_WARNING,

	/**
	 * 离线
	 */
	OFFLINE_EXAM,

	/**
	 * 支付成功
	 */
	PAY_SUCCESS,

}
