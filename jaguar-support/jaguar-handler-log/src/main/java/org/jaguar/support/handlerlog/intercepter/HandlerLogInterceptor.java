package org.jaguar.support.handlerlog.intercepter;

import com.alibaba.fastjson.JSONObject;
import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jaguar.commons.oauth2.model.SecurityUser;
import org.jaguar.commons.web.util.IpUtil;
import org.jaguar.support.handlerlog.model.HandlerLog;
import org.jaguar.support.handlerlog.service.HandlerLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;


/**
 * @author lvws
 * @since 2018/11/12.
 */
@Slf4j
@Component
public class HandlerLogInterceptor implements HandlerInterceptor {

    private static final String UN_AUTHORIZED_URL = "/login/401";

    /**
     * 当前线程访问信息
     */
    public static final ThreadLocal<HandlerLog> HANDLER_LOG = new NamedThreadLocal<>("HANDLER_LOG");

    private static final UASparser UAS_PARSER;

    static {
        try {
            UAS_PARSER = new UASparser(OnlineUpdater.getVendoredInputStream());
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    @Autowired
    protected HandlerLogService handlerLogService;

    private String getUserAgent(HttpServletRequest request) {
        UserAgentInfo userAgentInfo;
        try {
            userAgentInfo = UAS_PARSER.parse(request.getHeader("user-agent"));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }

        return userAgentInfo.getOsName() + " " + userAgentInfo.getType() + " " + userAgentInfo.getUaName();
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (handler instanceof HandlerMethod && !UN_AUTHORIZED_URL.equals(request.getRequestURI())) {
            ApiOperation apiOperation = ((HandlerMethod) handler).getMethod().getAnnotation(ApiOperation.class);

            HandlerLog handlerLog = new HandlerLog();
            handlerLog.setSessionId(request.getSession().getId());
            handlerLog.setAccessTime(LocalDateTime.now());
            handlerLog.setClientHost(IpUtil.getHost(request));
            handlerLog.setRequestUri(request.getServletPath());
            handlerLog.setApiOperation(apiOperation != null ? apiOperation.value() : null);
            handlerLog.setMethod(request.getMethod());
            handlerLog.setUserAgent(getUserAgent(request));
            handlerLog.setParameters(JSONObject.toJSONString(request.getParameterMap()));

            try {
                SecurityUser currentUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                handlerLog.setCreateBy(currentUser.getId());
                log.info("用户id: {}", handlerLog.getCreateBy());
            } catch (Exception ignored) {
                log.warn("用户[{}@{}]匿名请求", handlerLog.getClientHost(), handlerLog.getUserAgent());
            }

            HANDLER_LOG.set(handlerLog);
        }
        return true;
    }

    @Override
    public void afterCompletion(@NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response,
                                @NonNull Object handler, final Exception handlerException) {

        if (handler instanceof HandlerMethod && !(UN_AUTHORIZED_URL.equals(request.getRequestURI()))) {
            HandlerLog handlerLog = HANDLER_LOG.get();
            HANDLER_LOG.remove();

            long duration = Duration.between(handlerLog.getAccessTime(), LocalDateTime.now()).toMillis();
            handlerLog.setDuration(duration);
            handlerLog.setStatus(response.getStatus());
            handlerLog.setErrorMsg(ExceptionUtils.getMessage(handlerException));

            try {
                handlerLogService.saveLog(handlerLog);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
