package com.jaguar.process.service;

import com.jaguar.core.util.InstanceUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lvws on 2019/3/13.
 */
@Service
public class ExpressionService {

    public static final String DEFAULT_SPLIT_SEPARATION = ",";

    public List<String> stringToList(String str) {
        if (StringUtils.isBlank(str)) {
            return InstanceUtil.newArrayList();
        }

        String[] split = str.split(DEFAULT_SPLIT_SEPARATION);
        return Arrays.asList(split);
    }

}
