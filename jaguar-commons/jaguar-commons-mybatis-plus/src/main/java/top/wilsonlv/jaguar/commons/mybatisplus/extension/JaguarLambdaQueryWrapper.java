package top.wilsonlv.jaguar.commons.mybatisplus.extension;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * @author lvws
 * @since 2019/5/7.
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

    @Override
    public LambdaQueryWrapper<T> gt(boolean condition, SFunction<T, ?> column, Object val) {
        if (val == null) {
            return this;
        }

        return super.gt(condition, column, val);
    }

    @Override
    public LambdaQueryWrapper<T> ge(boolean condition, SFunction<T, ?> column, Object val) {
        if (val == null) {
            return this;
        }

        return super.ge(condition, column, val);
    }

    @Override
    public LambdaQueryWrapper<T> lt(boolean condition, SFunction<T, ?> column, Object val) {
        if (val == null) {
            return this;
        }

        return super.lt(condition, column, val);
    }

    @Override
    public LambdaQueryWrapper<T> le(boolean condition, SFunction<T, ?> column, Object val) {
        if (val == null) {
            return this;
        }

        return super.le(condition, column, val);
    }

    public static <T> JaguarLambdaQueryWrapper<T> newInstance() {
        return new JaguarLambdaQueryWrapper<T>();
    }
}
