package org.jaguar.commons.mybatisplus.extension;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * Created by lvws on 2019/5/7.
 */
public class JaguarLambdaQueryWrapper<T> extends LambdaQueryWrapper<T> {

    @Override
    public LambdaQueryWrapper<T> eq(boolean condition, SFunction<T, ?> column, Object val) {
        if (val == null) {
            return this;
        }
        if (val instanceof String && StringUtils.isBlank((String) val)) {
            return this;
        }
        return super.eq(condition, column, val);
    }

    @Override
    public LambdaQueryWrapper<T> like(boolean condition, SFunction<T, ?> column, Object val) {
        if (val == null) {
            return this;
        }
        if (val instanceof String && StringUtils.isBlank((String) val)) {
            return this;
        }

        return super.like(condition, column, val);
    }

    @Override
    public LambdaQueryWrapper<T> in(boolean condition, SFunction<T, ?> column, Collection<?> coll) {

        if (coll == null || CollectionUtils.isEmpty(coll)) {
            return this;
        }

        return super.in(condition, column, coll);
    }

    public static <T> JaguarLambdaQueryWrapper<T> newInstance() {
        return new JaguarLambdaQueryWrapper<T>();
    }
}
