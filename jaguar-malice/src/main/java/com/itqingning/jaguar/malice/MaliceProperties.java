package com.itqingning.jaguar.malice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lvws on 2019/4/22.
 */
@Configuration
@ConfigurationProperties(prefix = "malice")
public class MaliceProperties {

    /**
     * 启用ip拦截
     */
    private Boolean ipEnable = true;
    /**
     * 冻结期（s）
     */
    private Integer ipFreezingPeriod = 1800;

    /**
     * 访问maxRecentAccessTimeNum次的最小访问时间间隔（s）
     */
    private Integer ipMinRequestIntervalTime = 5;

    /**
     * 最多记录最近XX次的访问时间记录
     */
    private Integer ipMaxRecentAccessTimeNum = 100;

    /**
     * 启用session拦截
     */
    private Boolean sessionEnable = true;
    /**
     * 冻结期（s）
     */
    private Integer sessionFreezingPeriod = 1800;

    /**
     * 访问maxRecentAccessTimeNum次的最小访问时间间隔（s）
     */
    private Integer sessionMinRequestIntervalTime = 5;

    /**
     * 最多记录最近XX次的访问时间记录
     */
    private Integer sessionMaxRecentAccessTimeNum = 40;

    public Boolean getIpEnable() {
        return ipEnable;
    }

    public void setIpEnable(Boolean ipEnable) {
        this.ipEnable = ipEnable;
    }

    public Integer getIpFreezingPeriod() {
        return ipFreezingPeriod;
    }

    public void setIpFreezingPeriod(Integer ipFreezingPeriod) {
        this.ipFreezingPeriod = ipFreezingPeriod;
    }

    public Integer getIpMinRequestIntervalTime() {
        return ipMinRequestIntervalTime;
    }

    public void setIpMinRequestIntervalTime(Integer ipMinRequestIntervalTime) {
        this.ipMinRequestIntervalTime = ipMinRequestIntervalTime;
    }

    public Integer getIpMaxRecentAccessTimeNum() {
        return ipMaxRecentAccessTimeNum;
    }

    public void setIpMaxRecentAccessTimeNum(Integer ipMaxRecentAccessTimeNum) {
        this.ipMaxRecentAccessTimeNum = ipMaxRecentAccessTimeNum;
    }

    public Boolean getSessionEnable() {
        return sessionEnable;
    }

    public void setSessionEnable(Boolean sessionEnable) {
        this.sessionEnable = sessionEnable;
    }

    public Integer getSessionFreezingPeriod() {
        return sessionFreezingPeriod;
    }

    public void setSessionFreezingPeriod(Integer sessionFreezingPeriod) {
        this.sessionFreezingPeriod = sessionFreezingPeriod;
    }

    public Integer getSessionMinRequestIntervalTime() {
        return sessionMinRequestIntervalTime;
    }

    public void setSessionMinRequestIntervalTime(Integer sessionMinRequestIntervalTime) {
        this.sessionMinRequestIntervalTime = sessionMinRequestIntervalTime;
    }

    public Integer getSessionMaxRecentAccessTimeNum() {
        return sessionMaxRecentAccessTimeNum;
    }

    public void setSessionMaxRecentAccessTimeNum(Integer sessionMaxRecentAccessTimeNum) {
        this.sessionMaxRecentAccessTimeNum = sessionMaxRecentAccessTimeNum;
    }
}
