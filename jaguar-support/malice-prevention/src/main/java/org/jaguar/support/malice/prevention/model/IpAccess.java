package org.jaguar.support.malice.prevention.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lvws
 * @since 2018/11/9.
 */
@Data
public class IpAccess implements Serializable {

    /**
     * 地址
     */
    private String address;

    /**
     * 是否冻结
     */
    private Boolean freezing = false;

    /**
     * 冻结时间
     */
    private LocalDateTime freezingTime;

    public IpAccess() {
    }

    public IpAccess(String address) {
        this.address = address;
    }
}
