package org.jaguar.commons.dubbo.provider;

import org.jaguar.core.exception.CheckedException;

/**
 * Created by lvws on 2019/5/23.
 */
public interface IBaseProvider {
    Parameter execute(Parameter parameter) throws CheckedException;
}
