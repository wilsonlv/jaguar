package org.jaguar.commons.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by lvws on 2017/1/4.
 */
public class ExecutorServiceUtil {

    private static final ExecutorService service = Executors.newFixedThreadPool(20);

    public static Future execute(Runnable task) {
        Future<?> submit = null;
        try {
            submit = service.submit(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return submit;
    }

    public static Future execute(Callable task) {
        Future<?> submit = null;
        try {
            submit = service.submit(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return submit;
    }

}
