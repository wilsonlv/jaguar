package org.jaguar.cloud.job.executor.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jaguar.cloud.job.executor.properties.JobAdminProperties;
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

    private final JobAdminProperties jobAdminProperties;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(jobAdminProperties.getAdminAddresses());
        xxlJobSpringExecutor.setAppname(jobAdminProperties.getAppName());
        xxlJobSpringExecutor.setIp(jobAdminProperties.getIp());
        if (jobAdminProperties.getPort() != null) {
            xxlJobSpringExecutor.setPort(jobAdminProperties.getPort());
        }
        xxlJobSpringExecutor.setAccessToken(jobAdminProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(jobAdminProperties.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(jobAdminProperties.getLogRetentionDays());
        return xxlJobSpringExecutor;
    }

}
