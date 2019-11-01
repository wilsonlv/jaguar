package org.jaguar.commons.malice.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jaguar.commons.malice.config.MaliceProperties;
import org.jaguar.commons.malice.model.vo.SysSessionAccess;
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

    private static final String SESSION_ACCESS_PREFIX = "SysSessionAccess:";

    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private MaliceProperties maliceProperties;


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

        Long freezingPeriod = maliceProperties.getSessionFreezingPeriod() * 1000L;
        Integer maxRecentAccessTimeNum = maliceProperties.getSessionMaxRecentAccessTimeNum();
        Long minRequestIntervalTime = maliceProperties.getSessionMinRequestIntervalTime() * 1000L;

        LocalDateTime accessTime = LocalDateTime.now();

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
            if (sysSessionAccess.getFreezingTime().plusNanos(freezingPeriod).isAfter(accessTime)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                logger.warn("To intercept a malicious request : {} , host ip is : {} , sessionId is : {}", url, host, sessionId);
                return false;
            } else {
                sysSessionAccess.setFreezing(0);
                sysSessionAccess.setFreezingTime(null);
                redisCacheManager.set(sessionAccessKey, sysSessionAccess);
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

                if (sysSessionAccess.getFreezing() == 0) {
                    sysSessionAccess.setFreezing(1);
                    sysSessionAccess.setFreezingTime(accessTime);
                    redisCacheManager.set(sessionAccessKey, sysSessionAccess);
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
