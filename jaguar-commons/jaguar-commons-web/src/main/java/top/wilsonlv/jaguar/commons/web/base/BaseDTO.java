package top.wilsonlv.jaguar.commons.web.base;

import org.springframework.beans.BeanUtils;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2021/11/8
 */
public class BaseDTO implements Serializable {

    public <E> E toEntity(Class<E> clazz) {
        E entity;
        try {
            entity = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CheckedException(e);
        }
        BeanUtils.copyProperties(this, entity);
        return entity;
    }

}
