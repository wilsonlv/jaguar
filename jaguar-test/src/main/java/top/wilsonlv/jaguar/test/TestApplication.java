package top.wilsonlv.jaguar.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2021/7/12
 */
@Slf4j
@EnableAsync
@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication
public class TestApplication implements ApplicationRunner {

    @Autowired
    private KlockDemo klockDemo;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    public static void main(String[] args) {
//        SpringApplication.run(TestApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws InterruptedException {
        log.info("666开始");
        klockDemo.test(666);
        log.info("777开始");
        klockDemo.test(777);
        log.info("666又开始");
        klockDemo.test(666);

        Thread.sleep(1000);
        String key = "lock." + KlockDemo.class.getName() + ".test-" + 666;

        log.info(key);

        Boolean aBoolean = redisTemplate.hasKey(key);
        System.out.println(aBoolean);

//        lock.top.wilsonlv.jaguar.test.KlockDemo.test-666
//        lock.top.wilsonlv.jaguar.test.KlockDemo.test-666
    }

}
