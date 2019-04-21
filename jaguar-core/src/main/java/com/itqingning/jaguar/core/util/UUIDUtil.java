package com.itqingning.jaguar.core.util;

import java.util.UUID;

/**
 * Created by apple on 2017/2/28.
 */
public class UUIDUtil {

    public static String getId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
