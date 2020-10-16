package org.jaguar.support.malice.prevention.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.jaguar.commons.utils.IpUtil;
import org.jaguar.support.malice.prevention.config.MalicePreventionProperties;
import org.jaguar.support.malice.prevention.model.IpAccess;
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
public class MaliceIpInterceptor extends HandlerInterceptorAdapter {

    private static final String IP_ACCESS_PREFIX = "IpAccess:";

    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    private MalicePreventionProperties malicePreventionProperties;
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;


    private String namespace() {
        return serverProperties.getServlet().getApplicationDisplayName();
    }

    private String getIpAccessKey(String host) {
        return namespace() + ":" + IP_ACCESS_PREFIX + host;
    }

    private String getIpAccessTimeKey(String host) {
        return namespace() + ":" + IP_ACCESS_PREFIX + host + ":times";
    }

    /**
     * a：请求IP不在缓存中，将该IP加入到缓存中，并为该IP创建一个访问时间的队列
     * b：请求IP在缓存中{
     * b1：该IP为冻结状态，返回207
     * b2：该IP为非冻结状态{
     * b21：请求时间队列达到maxRecentAccessTimeNum{
     * 队首与队末的访问时间之差大于minRequestIntervalTime，通过；
     * 否则，冻结该IP，返回207
     * }
     * b22：请求时间队列没有达到maxRecentAccessTimeNum，通过；
     * }
     * }
     */
    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler)
            throws Exception {

        String url = request.getServletPath();
        if (url.endsWith("/unauthorized") || url.endsWith("/forbidden")) {
            return super.preHandle(request, response, handler);
        }

        String sessionId = request.getSession().getId();
        String host = IpUtil.getHost(request);

        long freezingPeriod = malicePreventionProperties.getIpFreezingPeriod() * 1000L;
        Integer maxRecentAccessTimeNum = malicePreventionProperties.getIpMaxRecentAccessTimeNum();
        long minRequestIntervalTime = malicePreventionProperties.getIpMinRequestIntervalTime() * 1000L;

        LocalDateTime accessTime = LocalDateTime.now();

        String ipAccessKey = getIpAccessKey(host);
        String ipAccessTimeKey = getIpAccessTimeKey(host);

        IpAccess ipAccess = new IpAccess(host);
        BoundValueOperations<String, Serializable> ipAccessKeyOperations = redisTemplate.boundValueOps(ipAccessKey);
        BoundListOperations<String, Serializable> ipAccessTimeKeyOperations = redisTemplate.boundListOps(ipAccessTimeKey);

        if (ipAccessKeyOperations.setIfAbsent(ipAccess)) {
            ipAccessTimeKeyOperations.rightPush(accessTime);
            return super.preHandle(request, response, handler);
        }

        ipAccess = (IpAccess) ipAccessKeyOperations.get();
        if (ipAccess.getFreezing()) {
            if (ipAccess.getFreezingTime().plusNanos(freezingPeriod).isAfter(accessTime)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                log.warn("To intercept a malicious request : {} , host ip is : {} , sessionId is : {}", url, host, sessionId);
                return false;
            } else {
                ipAccess.setFreezing(false);
                ipAccess.setFreezingTime(null);
                ipAccessKeyOperations.set(ipAccess);

                ipAccessTimeKeyOperations.leftPop();
                ipAccessTimeKeyOperations.rightPush(accessTime);
                return super.preHandle(request, response, handler);
            }
        }

        Long size = ipAccessTimeKeyOperations.size();
        if (size >= maxRecentAccessTimeNum) {
            //如果达到最大访问次数
            LocalDateTime firstDate = (LocalDateTime) ipAccessTimeKeyOperations.index(0);
            LocalDateTime endDate = (LocalDateTime) ipAccessTimeKeyOperations.index(maxRecentAccessTimeNum - 1);

            if (firstDate.plusNanos(minRequestIntervalTime).isBefore(endDate)) {
                ipAccessTimeKeyOperations.leftPop();
                ipAccessTimeKeyOperations.rightPush(accessTime);
                return super.preHandle(request, response, handler);
            } else {
                //如果时间差小于等于最小间隔，则可认为是恶意攻击
                ipAccess.setFreezing(true);
                ipAccess.setFreezingTime(accessTime);
                ipAccessKeyOperations.set(ipAccess);

                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                log.warn("To intercept a malicious request : {} , host ip is : {} , sessionId is : {}", url, host, sessionId);
                return false;
            }
        } else {
            ipAccessTimeKeyOperations.rightPush(accessTime);
            return super.preHandle(request, response, handler);
        }
    }
}
