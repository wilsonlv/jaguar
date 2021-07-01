package org.jaguar.cloud.job.executor.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2021/7/1
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jaguar.job.executor")
public class JobExecutorProperties implements Serializable {

    private String adminAddresses;

    private String appName;

    private String ip;

    private Integer port;

    private String accessToken;

    private String logPath;

    private Integer logRetentionDays = 30;

}
