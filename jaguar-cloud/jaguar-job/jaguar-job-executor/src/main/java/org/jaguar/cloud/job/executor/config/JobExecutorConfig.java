package org.jaguar.cloud.job.executor.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jaguar.cloud.job.executor.properties.JobProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lvws
 * @since 2021/7/1
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobExecutorConfig {

    private final JobProperties jobProperties;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(jobProperties.getAdminAddresses());
        xxlJobSpringExecutor.setAppname(jobProperties.getAppName());
        xxlJobSpringExecutor.setIp(jobProperties.getIp());
        if (jobProperties.getPort() != null) {
            xxlJobSpringExecutor.setPort(jobProperties.getPort());
        }
        xxlJobSpringExecutor.setAccessToken(jobProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(jobProperties.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(jobProperties.getLogRetentionDays());
        return xxlJobSpringExecutor;
    }

}
