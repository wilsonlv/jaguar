package org.jaguar.support.malice.prevention.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.jaguar.commons.utils.IpUtil;
import org.jaguar.support.malice.prevention.config.MalicePreventionProperties;
import org.jaguar.support.malice.prevention.model.SessionAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lvws
 * @since 2018/11/9.
 */
@Slf4j
@Component
public class MaliceSessionInterceptor extends HandlerInterceptorAdapter {

    private static final String SESSION_ACCESS_PREFIX = "SessionAccess:";

    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    private MalicePreventionProperties malicePreventionProperties;
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;


    private String namespace() {
        return serverProperties.getServlet().getApplicationDisplayName();
    }

    private String getSessionAccessKey(String sessionId) {
        return namespace() + ":" + SESSION_ACCESS_PREFIX + sessionId;
    }

    private String getSessionAccessTimeKey(String sessionId) {
        return namespace() + ":" + SESSION_ACCESS_PREFIX + sessionId + ":times";
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler)
            throws Exception {

        String url = request.getServletPath();
        if (url.endsWith("/unauthorized") || url.endsWith("/forbidden")) {
            return super.preHandle(request, response, handler);
        }

        String sessionId = request.getSession().getId();
        String host = IpUtil.getHost(request);

        long freezingPeriod = malicePreventionProperties.getSessionFreezingPeriod() * 1000L;
        Integer maxRecentAccessTimeNum = malicePreventionProperties.getSessionMaxRecentAccessTimeNum();
        long minRequestIntervalTime = malicePreventionProperties.getSessionMinRequestIntervalTime() * 1000L;

        LocalDateTime accessTime = LocalDateTime.now();

        String sessionAccessKey = getSessionAccessKey(sessionId);
        String sessionAccessTimeKey = getSessionAccessTimeKey(sessionId);

        SessionAccess sessionAccess = new SessionAccess(sessionId);
        BoundValueOperations<String, Serializable> sessionAccessKeyOperations = redisTemplate.boundValueOps(sessionAccessKey);
        BoundListOperations<String, Serializable> sessionAccessTimeKeyOperations = redisTemplate.boundListOps(sessionAccessTimeKey);

        if (sessionAccessKeyOperations.setIfAbsent(sessionAccess)) {
            sessionAccessTimeKeyOperations.rightPush(accessTime);
            return super.preHandle(request, response, handler);
        }

        sessionAccess = (SessionAccess) sessionAccessKeyOperations.get();
        if (sessionAccess.getFreezing()) {
            if (sessionAccess.getFreezingTime().plusNanos(freezingPeriod).isAfter(accessTime)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                log.warn("To intercept a malicious request : {} , host ip is : {} , sessionId is : {}", url, host, sessionId);
                return false;
            } else {
                sessionAccess.setFreezing(false);
                sessionAccess.setFreezingTime(null);
                sessionAccessKeyOperations.set(sessionAccess);

                sessionAccessTimeKeyOperations.leftPop();
                sessionAccessTimeKeyOperations.rightPush(sessionAccess);
                return super.preHandle(request, response, handler);
            }
        }

        Long size = sessionAccessTimeKeyOperations.size();
        if (size >= maxRecentAccessTimeNum) {
            //如果达到最大访问次数
            LocalDateTime firstDate = (LocalDateTime) sessionAccessTimeKeyOperations.index(0);
            LocalDateTime endDate = (LocalDateTime) sessionAccessTimeKeyOperations.index(maxRecentAccessTimeNum - 1);

            if (firstDate.plusNanos(minRequestIntervalTime).isBefore(endDate)) {
                sessionAccessTimeKeyOperations.leftPop();
                sessionAccessTimeKeyOperations.rightPush(accessTime);
                return super.preHandle(request, response, handler);
            } else {
                //如果时间差小于等于最小间隔，则可认为是恶意攻击
                sessionAccess.setFreezing(true);
                sessionAccess.setFreezingTime(accessTime);
                sessionAccessKeyOperations.set(sessionAccess);

                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                log.warn("To intercept a malicious request : {} , host ip is : {} , sessionId is : {}", url, host, sessionId);
                return false;
            }
        } else {
            sessionAccessTimeKeyOperations.rightPush(sessionAccessTimeKey, accessTime);
            return super.preHandle(request, response, handler);
        }
    }
}
