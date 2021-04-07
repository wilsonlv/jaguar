package org.jaguar.support.handlerlog.intercepter;

import com.alibaba.fastjson.JSONObject;
import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jaguar.commons.utils.ExecutorServiceUtil;
import org.jaguar.commons.utils.IpUtil;
import org.jaguar.support.handlerlog.model.HandlerLog;
import org.jaguar.support.handlerlog.service.HandlerLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

;

/**
 * @author lvws
 * @since 2018/11/12.
 */
@Slf4j
@Component
public class HandlerLogInterceptor extends HandlerInterceptorAdapter {

    private static final String ERROR = "/error";

    /**
     * 当前线程访问信息
     */
    public static final ThreadLocal<HandlerLog> HANDLER_LOG = new NamedThreadLocal<>("HANDLER_LOG");

    @Autowired
    protected HandlerLogService handlerLogService;

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

        return userAgentInfo.getOsName() + " " +
                userAgentInfo.getType() + " " +
                userAgentInfo.getUaName();
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler)
            throws Exception {

        if (handler instanceof HandlerMethod) {
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
//                handlerLog.setCreateBy(LoginUtil.getCurrentUser());
                log.info("用户id: {}", handlerLog.getCreateBy());
            } catch (Exception ignored) {
                log.warn("用户[{}@{}]匿名请求", handlerLog.getClientHost(), handlerLog.getUserAgent());
            }

            HANDLER_LOG.set(handlerLog);
        }

        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(@NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response,
                                @NonNull Object handler, final Exception handlerException) throws Exception {

        if (handler instanceof HandlerMethod && !(ERROR.equals(request.getRequestURI()))) {
            final HandlerLog handlerLog = HANDLER_LOG.get();
            handlerLog.setStatus(response.getStatus());

            HANDLER_LOG.remove();

            LocalDateTime endTime = LocalDateTime.now();
            ExecutorServiceUtil.execute(() -> {
                long duration = Duration.between(handlerLog.getAccessTime(), endTime).toMillis();
                handlerLog.setDuration(duration);
                handlerLog.setErrorMsg(ExceptionUtils.getMessage(handlerException));
                handlerLog.setCreateTime(endTime);
                handlerLog.setDeleted(false);
                try {
                    handlerLogService.saveLog(handlerLog);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
        super.afterCompletion(request, response, handler, handlerException);
    }

}
