package org.jaguar.support.malice.prevention.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lvws on 2019/4/22.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "malice.prevention")
public class MalicePreventionProperties {

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

}
