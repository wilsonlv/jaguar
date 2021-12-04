package top.wilsonlv.jaguar.cloud.job.executor.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.wilsonlv.jaguar.cloud.job.executor.properties.JobExecutorProperties;
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

    private final JobExecutorProperties jobExecutorProperties;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(jobExecutorProperties.getAdminAddresses());
        xxlJobSpringExecutor.setAppname(jobExecutorProperties.getAppName());
        xxlJobSpringExecutor.setIp(jobExecutorProperties.getIp());
        if (jobExecutorProperties.getPort() != null) {
            xxlJobSpringExecutor.setPort(jobExecutorProperties.getPort());
        }
        xxlJobSpringExecutor.setAccessToken(jobExecutorProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(jobExecutorProperties.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(jobExecutorProperties.getLogRetentionDays());
        return xxlJobSpringExecutor;
    }

}
