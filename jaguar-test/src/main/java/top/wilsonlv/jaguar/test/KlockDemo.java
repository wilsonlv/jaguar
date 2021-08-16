package top.wilsonlv.jaguar.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.klock.annotation.Klock;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lvws
 * @since 2021/8/14
 */
@Slf4j
@Component
public class KlockDemo {

//    @Async
//    public void test(Integer id){
//        SpringContext.getBean(KlockDemo.class).lock(id);
//    }

    @Async
    @Klock(keys = "#id")
    @Transactional
    public void test(Integer id) {
        log.info("{}进来啦", id);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("{}执行完了", id);
    }

    //lock.top.wilsonlv.jaguar.test.KlockDemo.test-123

}
