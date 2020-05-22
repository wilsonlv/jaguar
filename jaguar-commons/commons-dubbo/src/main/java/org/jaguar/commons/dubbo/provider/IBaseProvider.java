package org.jaguar.commons.dubbo.provider;

import org.jaguar.core.exception.CheckedException;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2019/5/23.
 */
public interface IBaseProvider extends Serializable {

    /**
     * 调用dubbo提供者
     *
     * @param parameter 中间件
     * @return 中间件
     * @throws CheckedException e
     */
    Parameter execute(Parameter parameter) throws CheckedException;
}
