package com.jaguar.web.aspect;

import com.jaguar.core.base.BaseModel;
import com.jaguar.web.WebUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Created by lvws on 2019/4/17.
 */
@Aspect
@Component
public class BaseServiceAspect {

    @Before("execution(* com.jaguar.core.base.BaseService.update(..))")
    public void update(JoinPoint joinPoint) {
        BaseModel model = (BaseModel) joinPoint.getArgs()[0];

        Long currentUser = WebUtil.getCurrentUser();

        model.setUpdateBy(currentUser);
        if (model.getId() != null) {
            model.setCreateBy(currentUser);
        }
    }

}