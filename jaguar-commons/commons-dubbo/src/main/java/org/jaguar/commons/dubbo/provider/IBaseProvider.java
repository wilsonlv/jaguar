package org.jaguar.commons.dubbo.provider;

import org.jaguar.core.exception.CheckedException;

/**
 * @author lvws
 * @since 2019/5/23.
 */
public interface IBaseProvider {

    /**
     * 调用接口
     */
    Parameter execute(Parameter parameter) throws CheckedException;
}
