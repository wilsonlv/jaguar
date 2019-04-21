package com.itqingning.jaguar.sys.log.intercepter;

import com.alibaba.fastjson.JSON;
import com.itqingning.jaguar.core.http.HttpCode;
import com.itqingning.jaguar.core.util.DateUtil;
import com.itqingning.jaguar.core.util.ExceptionUtil;
import com.itqingning.jaguar.core.util.ExecutorServiceUtil;
import com.itqingning.jaguar.sys.log.model.SysLog;
import com.itqingning.jaguar.sys.log.service.SysLogService;
import com.itqingning.jaguar.web.JsonResult;
import com.itqingning.jaguar.web.WebUtil;
import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by lvws on 2018/11/12.
 */
@Component
public class SysLogInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LogManager.getLogger(getClass());

    /**
     * 当前线程访问信息
     */
    public static final ThreadLocal<SysLog> SYS_LOG = new NamedThreadLocal<>("SYS_LOG");

    @Autowired
    protected SysLogService sysLogService;

    private static UASparser uasParser = null;

    static {
        try {
            uasParser = new UASparser(OnlineUpdater.getVendoredInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getUserAgent(HttpServletRequest request) {
        UserAgentInfo userAgentInfo = null;
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

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (handler instanceof HandlerMethod) {
            ApiOperation apiOperation = ((HandlerMethod) handler).getMethod().getAnnotation(ApiOperation.class);

            SysLog sysLog = new SysLog();
            sysLog.setSessionId(request.getSession().getId());
            sysLog.setAccessTime(new Date());
            sysLog.setClientHost(WebUtil.getHost(request));
            sysLog.setRequestUri(request.getServletPath());
            sysLog.setApiOperation(apiOperation != null ? apiOperation.value() : null);
            sysLog.setMethod(request.getMethod());
            sysLog.setUserAgent(getUserAgent(request));
            sysLog.setParameters(JSON.toJSONString(request.getParameterMap()));
            sysLog.setCreateBy(WebUtil.getCurrentUser());

            SYS_LOG.set(sysLog);
        }

        return super.preHandle(request, response, handler);
    }

    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, Object handler,
                                final Exception ex) throws Exception {

        if (handler instanceof HandlerMethod && !(request.getRequestURI().equals("/error"))) {
            final SysLog sysLog = SYS_LOG.get();

            HttpCode httpCode = JsonResult.HTTP_CODE.get();
            sysLog.setStatus(httpCode != null ? httpCode.value() : response.getStatus());

            Date endTime = new Date();
            Long duration = endTime.getTime() - sysLog.getAccessTime().getTime();

            if (sysLog.getRequestUri().contains("/unauthorized")) {
                logger.warn("用户[{}@{}]没有登录", sysLog.getClientHost(), sysLog.getUserAgent());
            } else if (sysLog.getRequestUri().contains("/forbidden")) {
                logger.warn("用户[{}@{}@{}]没有权限", sysLog.getCreateBy(), sysLog.getClientHost(), sysLog.getUserAgent());
            } else {
                ExecutorServiceUtil.execute(() -> {

                    sysLog.setDuration(duration);
                    sysLog.setRemark(ExceptionUtil.getStackTraceAsString(ex));
                    sysLogService.saveLog(sysLog);
                });
            }

            String message = "响应uri: {}; 开始时间: {}; 结束时间: {}; 耗时: {}s;";
            logger.info(message, sysLog.getRequestUri(),
                    DateUtil.format(sysLog.getAccessTime(), DateUtil.DatePattern.HH_MM_SS_SSS),
                    DateUtil.format(endTime, DateUtil.DatePattern.HH_MM_SS_SSS), duration / 1000.00);

        }
        super.afterCompletion(request, response, handler, ex);
    }


}
