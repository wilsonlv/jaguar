package org.jaguar.support.fieldeditlog.intercepter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.jaguar.core.web.LoginUtil;
import org.jaguar.support.fieldeditlog.service.FieldEditLogService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lvws
 * @since 2021/3/30.
 */
@Slf4j
@Component
public class FieldEditLogInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler)
            throws Exception {

        if (handler instanceof HandlerMethod) {
            try {
                FieldEditLogService.CURRENT_USER.set(LoginUtil.getCurrentUser());
            } catch (ShiroException ignored) {
                log.debug("匿名用户");
            }
        }

        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(@NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response,
                                @NonNull Object handler, final Exception handlerException) throws Exception {

        if (handler instanceof HandlerMethod) {
            FieldEditLogService.CURRENT_USER.remove();
        }
        super.afterCompletion(request, response, handler, handlerException);
    }

}
