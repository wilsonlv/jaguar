package com.jaguar.malice;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lvws on 2018/11/9.
 */
public class SysIpAccess implements Serializable {

    /**
     * 地址
     */
    private String address;

    /**
     * 冻结IP
     */
    private Integer freezing;

    /**
     * 冻结时间
     */
    private Date freezingTime;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
