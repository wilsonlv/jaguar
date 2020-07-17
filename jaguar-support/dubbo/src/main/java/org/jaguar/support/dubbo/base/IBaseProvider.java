package org.jaguar.support.dubbo.base;

import org.apache.shiro.authc.AuthenticationException;
import org.jaguar.core.exception.BadRequestException;
import org.jaguar.core.exception.CheckedException;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2019/5/23.
 */
public interface IBaseProvider extends Serializable {

    String VERSION = "1.0.0";

    /**
     * 调用dubbo提供者
     *
     * @param parameter 中间件
     * @return 中间件
     * @throws CheckedException        e
     * @throws BadRequestException     e
     * @throws AuthenticationException e
     */
    Parameter execute(Parameter parameter) throws CheckedException, BadRequestException, AuthenticationException;
}
