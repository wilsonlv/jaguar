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
    private Integer freezing;

    /**
     * 冻结时间
     */
    private LocalDateTime freezingTime;

}
