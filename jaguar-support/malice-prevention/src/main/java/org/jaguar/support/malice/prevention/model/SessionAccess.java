package org.jaguar.support.malice.prevention.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lvws
 * @since 2018/11/9.
 */
@Data
public class SessionAccess implements Serializable {

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 冻结IP
     */
    private Boolean freezing = false;

    /**
     * 冻结时间
     */
    private LocalDateTime freezingTime;

    public SessionAccess() {
    }

    public SessionAccess(String sessionId) {
        this.sessionId = sessionId;
    }
}
