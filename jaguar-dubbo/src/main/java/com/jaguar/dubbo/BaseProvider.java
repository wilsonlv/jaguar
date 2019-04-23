package com.jaguar.dubbo;

import com.jaguar.core.exception.BusinessException;

/**
 * @author lws
 */
public interface BaseProvider {
    Parameter execute(Parameter parameter) throws BusinessException;
}
