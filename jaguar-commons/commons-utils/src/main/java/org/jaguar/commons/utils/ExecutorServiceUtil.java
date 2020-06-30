package org.jaguar.commons.utils;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

/**
 * @author lvws
 * @since 2017/1/4.
 */
public class ExecutorServiceUtil {

    private static final ThreadFactory FACTORY = new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build();

    public static final ScheduledExecutorService SERVICE = new ScheduledThreadPoolExecutor(20, FACTORY);

    public static Future<?> execute(Runnable task) {
        Future<?> submit = null;
        try {
            submit = SERVICE.submit(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return submit;
    }

    public static Future<?> execute(Callable<?> task) {
        Future<?> submit = null;
        try {
            submit = SERVICE.submit(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return submit;
    }

}
