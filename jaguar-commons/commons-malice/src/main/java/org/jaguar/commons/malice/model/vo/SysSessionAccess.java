package org.jaguar.commons.malice.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by lvws on 2018/11/9.
 */
@Data
public class SysSessionAccess implements Serializable {

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
