package top.wilsonlv.jaguar.cloud.job.executor.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lvws
 * @since 2021/7/1
 */
@Slf4j
@Component
@AllArgsConstructor
public class DemoJob {

    @XxlJob("demoJob")
    public void demoJob() {
        log.info("demoJob");
    }

}
