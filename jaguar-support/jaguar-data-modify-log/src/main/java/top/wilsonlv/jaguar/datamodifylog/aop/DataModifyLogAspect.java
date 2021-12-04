package top.wilsonlv.jaguar.datamodifylog.aop;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import top.wilsonlv.jaguar.basecrud.BaseModel;
import top.wilsonlv.jaguar.basecrud.BaseService;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;
import top.wilsonlv.jaguar.datamodifylog.entity.DataModifyLoggable;
import top.wilsonlv.jaguar.datamodifylog.service.DataModifyLogService;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * @author lvws
 * @since 2021/11/22
 */
@Slf4j
@Aspect
@Order(200)
@Component
public class DataModifyLogAspect {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private DataModifyLogService<DataModifyLoggable> dataModifyLogService;

    @Pointcut("execution(* top.wilsonlv.jaguar..*.mapper.*.updateById(..))")
    public void updateById() {
    }

    @Pointcut("execution(* top.wilsonlv.jaguar.basecrud.BatchOperation.batchUpdateById(..))")
    public void batchUpdateById() {
    }

    @Around("updateById()")
    public Object updateByIdAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args[0] instanceof DataModifyLoggable) {
            DataModifyLoggable dataModifyLoggable = (DataModifyLoggable) args[0];

            String entityName = StrUtil.lowerFirst(dataModifyLoggable.getClass().getSimpleName());
            BaseService<?, ?> service = (BaseService<?, ?>) applicationContext.getBean(entityName + "Service");
            BaseModel org = service.getById(dataModifyLoggable.getId());

            try {
                dataModifyLogService.log((DataModifyLoggable) org, dataModifyLoggable);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
                throw new CheckedException(e.getMessage());
            }
        }
        return joinPoint.proceed(args);
    }

    @Around("batchUpdateById()")
    public Object batchUpdateByIdAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Collection<?> entityList = (Collection<?>) args[3];
        for (Object next : entityList) {
            if (next instanceof DataModifyLoggable) {
                dataModifyLogService.log(null, (DataModifyLoggable) next);
            } else {
                break;
            }
        }
        return joinPoint.proceed(args);
    }

}
