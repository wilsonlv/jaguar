package org.jaguar.modules.handlerlog.intercepter;

import com.alibaba.fastjson.JSONObject;
import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jaguar.commons.shiro.config.ShiroProperties;
import org.jaguar.commons.utils.DateUtil;
import org.jaguar.commons.utils.ExecutorServiceUtil;
import org.jaguar.commons.utils.IPUtil;
import org.jaguar.core.base.BaseService;
import org.jaguar.core.web.LoginUtil;
import org.jaguar.modules.handlerlog.model.HandlerLog;
import org.jaguar.modules.handlerlog.service.HandlerLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Created by lvws on 2018/11/12.
 */
@Slf4j
@Component
public class HandlerLogInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private ShiroProperties shiroProperties;

    /**
     * 当前线程访问信息
     */
    public static final ThreadLocal<HandlerLog> HANDLER_LOG = new NamedThreadLocal<>("HANDLER_LOG");

    @Autowired
    protected HandlerLogService sysLogService;

    private static UASparser uasParser = null;

    static {
        try {
            uasParser = new UASparser(OnlineUpdater.getVendoredInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getUserAgent(HttpServletRequest request) {
        UserAgentInfo userAgentInfo;
        try {
            userAgentInfo = uasParser.parse(request.getHeader("user-agent"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return new StringBuffer()
                .append(userAgentInfo.getOsName()).append(" ")
                .append(userAgentInfo.getType()).append(" ")
                .append(userAgentInfo.getUaName()).toString();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (handler instanceof HandlerMethod) {
            ApiOperation apiOperation = ((HandlerMethod) handler).getMethod().getAnnotation(ApiOperation.class);

            HandlerLog handlerLog = new HandlerLog();
            handlerLog.setSessionId(request.getSession().getId());
            handlerLog.setAccessTime(LocalDateTime.now());
            handlerLog.setClientHost(IPUtil.getHost(request));
            handlerLog.setRequestUri(request.getServletPath());
            handlerLog.setApiOperation(apiOperation != null ? apiOperation.value() : null);
            handlerLog.setMethod(request.getMethod());
            handlerLog.setUserAgent(getUserAgent(request));
            handlerLog.setParameters(JSONObject.toJSONString(request.getParameterMap()));
            try {
                handlerLog.setCreateBy(LoginUtil.getCurrentUser());
            } catch (Exception ignored) {
                log.warn("用户[{}@{}]匿名请求", handlerLog.getClientHost(), handlerLog.getUserAgent());
            }

            HANDLER_LOG.set(handlerLog);
            BaseService.CURRENT_USER.set(handlerLog.getCreateBy());
        }

        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, Object handler,
                                final Exception ex) throws Exception {

        if (handler instanceof HandlerMethod && !("/error".equals(request.getRequestURI()))) {
            final HandlerLog handlerLog = HANDLER_LOG.get();
            HANDLER_LOG.remove();
            BaseService.CURRENT_USER.remove();

            handlerLog.setStatus(response.getStatus());

            LocalDateTime endTime = LocalDateTime.now();
            Long duration = Duration.between(endTime, handlerLog.getAccessTime()).toMillis();

            if (handlerLog.getRequestUri().contains(shiroProperties.getLoginUrl())) {
                log.warn("用户[{}@{}]没有登录", handlerLog.getClientHost(), handlerLog.getUserAgent());
            } else if (handlerLog.getRequestUri().contains(shiroProperties.getUnauthorizedUrl())) {
                log.warn("用户[{}@{}@{}]没有权限", handlerLog.getCreateBy(), handlerLog.getClientHost(), handlerLog.getUserAgent());
            } else {
                ExecutorServiceUtil.execute(() -> {
                    handlerLog.setDuration(duration);
                    handlerLog.setErrorMsg(ex != null ? ex.getMessage() : null);
                    sysLogService.saveLog(handlerLog);
                });
            }

            String message = "响应uri: {}; 开始时间: {}; 结束时间: {}; 耗时: {}s;";
            log.info(message, handlerLog.getRequestUri(),
                    DateUtil.formatDateTime(handlerLog.getAccessTime(), DateUtil.DateTimePattern.YYYY_MM_DD_HH_MM_SS_SSS),
                    DateUtil.formatDateTime(endTime, DateUtil.DateTimePattern.YYYY_MM_DD_HH_MM_SS_SSS),
                    duration / 1000.00);

        }
        super.afterCompletion(request, response, handler, ex);
    }

}
