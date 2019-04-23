package com.itqingning.jaguar.malice;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lvws on 2018/11/9.
 */
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
    private Date freezingTime;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getFreezing() {
        return freezing;
    }

    public void setFreezing(Integer freezing) {
        this.freezing = freezing;
    }

    public Date getFreezingTime() {
        return freezingTime;
    }

    public void setFreezingTime(Date freezingTime) {
        this.freezingTime = freezingTime;
    }

}
