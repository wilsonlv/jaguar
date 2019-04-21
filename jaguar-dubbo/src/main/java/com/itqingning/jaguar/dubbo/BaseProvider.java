package com.itqingning.jaguar.dubbo;

import com.itqingning.jaguar.core.exception.BusinessException;

/**
 * @author lws
 */
public interface BaseProvider {
    Parameter execute(Parameter parameter) throws BusinessException;
}
