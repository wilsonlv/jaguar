package org.jaguar.cloud.websocket.sdk.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author lvws
 * @since 2021/4/29
 */
@RequiredArgsConstructor
public enum WebsocketPage {

    /**
     * 学生主页
     */
    ADMIN_INDEX(60);

    @Getter
    private final int heartbeat;

    public long getExpire() {
        return this.heartbeat + 5;
    }

}
