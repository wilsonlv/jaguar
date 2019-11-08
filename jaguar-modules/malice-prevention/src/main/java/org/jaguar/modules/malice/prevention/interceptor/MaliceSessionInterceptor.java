package org.jaguar.modules.malice.prevention.interceptor;

import org.jaguar.modules.malice.prevention.config.MalicePreventionProperties;
import org.jaguar.modules.malice.prevention.model.SessionAccess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jaguar.commons.redis.cache.RedisCacheManager;
import org.jaguar.commons.utils.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * Created by lvws on 2018/11/9.
 */
@Component
public class MaliceSessionInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LogManager.getLogger(getClass());

    private static final String SESSION_ACCESS_PREFIX = "SessionAccess:";

    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private MalicePreventionProperties malicePreventionProperties;


    private String namespace() {
        return redisCacheManager.getNamespace();
    }

    private String getSessionAccessKey(String sessionId) {
        return new StringBuilder(namespace()).append(":").append(SESSION_ACCESS_PREFIX).append(sessionId).toString();
    }

    private String getSessionAccessTimeKey(String sessionId) {
        return new StringBuilder(namespace()).append(":").append(SESSION_ACCESS_PREFIX).append(sessionId).append(":times").toString();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String url = request.getServletPath();
        if (url.endsWith("/unauthorized") || url.endsWith("/forbidden")) {
            return super.preHandle(request, response, handler);
        }

        String sessionId = request.getSession().getId();
        String host = IPUtil.getHost(request);

        Long freezingPeriod = malicePreventionProperties.getSessionFreezingPeriod() * 1000L;
        Integer maxRecentAccessTimeNum = malicePreventionProperties.getSessionMaxRecentAccessTimeNum();
        Long minRequestIntervalTime = malicePreventionProperties.getSessionMinRequestIntervalTime() * 1000L;

        LocalDateTime accessTime = LocalDateTime.now();

        String sessionAccessKey = getSessionAccessKey(sessionId);
        String sessionAccessTimeKey = getSessionAccessTimeKey(sessionId);

        SessionAccess sessionAccess = (SessionAccess) redisCacheManager.get(sessionAccessKey);
        if (sessionAccess == null) {
            sessionAccess = new SessionAccess();
            sessionAccess.setSessionId(sessionId);
            sessionAccess.setFreezing(0);
            redisCacheManager.set(sessionAccessKey, sessionAccess);

            redisCacheManager.rightPush(sessionAccessTimeKey, accessTime);
            return super.preHandle(request, response, handler);
        }

        if (sessionAccess.getFreezing() == 1) {
            if (sessionAccess.getFreezingTime().plusNanos(freezingPeriod).isAfter(accessTime)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                logger.warn("To intercept a malicious request : {} , host ip is : {} , sessionId is : {}", url, host, sessionId);
                return false;
            } else {
                sessionAccess.setFreezing(0);
                sessionAccess.setFreezingTime(null);
                redisCacheManager.set(sessionAccessKey, sessionAccess);
            }
        }

        if (maxRecentAccessTimeNum <= redisCacheManager.lsize(sessionAccessTimeKey)) {
            //如果达到最大访问次数

            LocalDateTime firstDate = (LocalDateTime) redisCacheManager.lrange(sessionAccessTimeKey, 0, 1).get(0);
            LocalDateTime endDate = (LocalDateTime) redisCacheManager.lrange(sessionAccessTimeKey, maxRecentAccessTimeNum - 1, maxRecentAccessTimeNum).get(0);

            if (firstDate.plusNanos(minRequestIntervalTime).isBefore(endDate)) {
                redisCacheManager.leftPop(sessionAccessTimeKey);
                redisCacheManager.rightPush(sessionAccessTimeKey, accessTime);
            } else {
                //如果时间差小于等于最小间隔，则可认为是恶意攻击

                if (sessionAccess.getFreezing() == 0) {
                    sessionAccess.setFreezing(1);
                    sessionAccess.setFreezingTime(accessTime);
                    redisCacheManager.set(sessionAccessKey, sessionAccess);
                }

                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                logger.warn("To intercept a malicious request : {} , host ip is : {} , sessionId is : {}", url, host, sessionId);
                return false;
            }
        } else {
            redisCacheManager.rightPush(sessionAccessTimeKey, accessTime);
        }

        return super.preHandle(request, response, handler);
    }
}
