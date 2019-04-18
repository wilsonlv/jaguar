package com.itqingning.jaguar.interceptor;

import com.itqingning.core.http.HttpCode;
import com.itqingning.jaguar.web.WebUtil;
import com.itqingning.jaguar.redis.RedisCacheManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by lvws on 2018/11/9.
 */
@Component
public class MaliceSessionInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LogManager.getLogger(getClass());

    private static final String SESSION_ACCESS_PREFIX = "SysSessionAccess:";

    @Autowired
    private RedisCacheManager redisCacheManager;

    @Value("${interceptor.malice.namespace}")
    private String namespace;

    /**
     * 冻结期（s）
     */
    @Value("${interceptor.malice.session.freezingPeriod}")
    private Integer freezingPeriod;

    /**
     * 访问maxRecentAccessTimeNum次的最小访问时间间隔（s）
     */
    @Value("${interceptor.malice.session.minRequestIntervalTime}")
    private Integer minRequestIntervalTime;

    /**
     * 最多记录最近XX次的访问时间记录
     */
    @Value("${interceptor.malice.session.maxRecentAccessTimeNum}")
    private Integer maxRecentAccessTimeNum;


    private String getSessionAccessKey(String sessionId) {
        return new StringBuilder(namespace).append(":").append(SESSION_ACCESS_PREFIX).append(sessionId).toString();
    }

    private String getSessionAccessTimeKey(String sessionId) {
        return new StringBuilder(namespace).append(":").append(SESSION_ACCESS_PREFIX).append(sessionId).append(":times").toString();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String url = request.getServletPath();
        if (url.endsWith("/unauthorized") || url.endsWith("/forbidden")) {
            return super.preHandle(request, response, handler);
        }

        String sessionId = request.getSession().getId();
        String host = WebUtil.getHost(request);

        Date accessTime = new Date();

        String sessionAccessKey = getSessionAccessKey(sessionId);
        String sessionAccessTimeKey = getSessionAccessTimeKey(sessionId);

        SysSessionAccess sysSessionAccess = (SysSessionAccess) redisCacheManager.get(sessionAccessKey);
        if (sysSessionAccess == null) {
            sysSessionAccess = new SysSessionAccess();
            sysSessionAccess.setSessionId(sessionId);
            sysSessionAccess.setFreezing(0);
            redisCacheManager.set(sessionAccessKey, sysSessionAccess);

            redisCacheManager.rightPush(sessionAccessTimeKey, accessTime);
            return super.preHandle(request, response, handler);
        }

        if (sysSessionAccess.getFreezing() == 1) {
            if ((sysSessionAccess.getFreezingTime().getTime() + freezingPeriod * 1000) > accessTime.getTime()) {
                response.setStatus(HttpCode.MULTI_STATUS.value());
                logger.warn("To intercept a malicious request : {} , host ip is : {} , sessionId is : {}", url, host, sessionId);
                return false;
            } else {
                sysSessionAccess.setFreezing(0);
                sysSessionAccess.setFreezingTime(null);
                redisCacheManager.set(sessionAccessKey, sysSessionAccess);
            }
        }

        if (maxRecentAccessTimeNum <= redisCacheManager.lsize(sessionAccessTimeKey)) {//如果达到最大访问次数
            Date firstDate = (Date) redisCacheManager.lrange(sessionAccessTimeKey, 0, 1).get(0);
            Date endDate = (Date) redisCacheManager.lrange(sessionAccessTimeKey, maxRecentAccessTimeNum - 1, maxRecentAccessTimeNum).get(0);
            if ((endDate.getTime() - firstDate.getTime()) / 1000 > minRequestIntervalTime) {
                redisCacheManager.leftPop(sessionAccessTimeKey);
                redisCacheManager.rightPush(sessionAccessTimeKey, accessTime);
            } else {//如果时间差小于等于最小间隔，则可认为是恶意攻击
                if (sysSessionAccess.getFreezing() == 0) {
                    sysSessionAccess.setFreezing(1);
                    sysSessionAccess.setFreezingTime(accessTime);
                    redisCacheManager.set(sessionAccessKey, sysSessionAccess);
                }

                response.setStatus(HttpCode.MULTI_STATUS.value());
                logger.warn("To intercept a malicious request : {} , host ip is : {} , sessionId is : {}", url, host, sessionId);
                return false;
            }
        } else {
            redisCacheManager.rightPush(sessionAccessTimeKey, accessTime);
        }

        return super.preHandle(request, response, handler);

    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setRedisCacheManager(RedisCacheManager redisCacheManager) {
        this.redisCacheManager = redisCacheManager;
    }

    public void setFreezingPeriod(Integer freezingPeriod) {
        this.freezingPeriod = freezingPeriod;
    }

    public void setMinRequestIntervalTime(Integer minRequestIntervalTime) {
        this.minRequestIntervalTime = minRequestIntervalTime;
    }

    public void setMaxRecentAccessTimeNum(Integer maxRecentAccessTimeNum) {
        this.maxRecentAccessTimeNum = maxRecentAccessTimeNum;
    }
}
