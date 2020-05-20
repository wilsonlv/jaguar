package org.jaguar.modules.malice.prevention.interceptor;

import org.jaguar.modules.malice.prevention.config.MalicePreventionProperties;
import org.jaguar.modules.malice.prevention.model.IpAccess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jaguar.commons.redis.cache.RedisCacheManager;
import org.jaguar.commons.utils.IpUtil;
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
public class MaliceIpInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LogManager.getLogger(getClass());

    private static final String IP_ACCESS_PREFIX = "IpAccess:";

    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private MalicePreventionProperties malicePreventionProperties;

    private String namespace() {
        return redisCacheManager.getNamespace();
    }

    private String getIpAccessKey(String host) {
        return new StringBuilder(namespace()).append(":").append(IP_ACCESS_PREFIX).append(host).toString();
    }

    private String getIpAccessTimeKey(String host) {
        return new StringBuilder(namespace()).append(":").append(IP_ACCESS_PREFIX).append(host).append(":times").toString();
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String url = request.getServletPath();
        if (url.endsWith("/unauthorized") || url.endsWith("/forbidden")) {
            return super.preHandle(request, response, handler);
        }

        String sessionId = request.getSession().getId();
        String host = IpUtil.getHost(request);

        Long freezingPeriod = malicePreventionProperties.getIpFreezingPeriod() * 1000L;
        Integer maxRecentAccessTimeNum = malicePreventionProperties.getIpMaxRecentAccessTimeNum();
        Long minRequestIntervalTime = malicePreventionProperties.getIpMinRequestIntervalTime() * 1000L;

        LocalDateTime accessTime = LocalDateTime.now();

        String ipAccessKey = getIpAccessKey(host);
        String ipAccessTimeKey = getIpAccessTimeKey(host);

        IpAccess ipAccess = (IpAccess) redisCacheManager.get(ipAccessKey);
        if (ipAccess == null) {
            ipAccess = new IpAccess();
            ipAccess.setAddress(host);
            ipAccess.setFreezing(0);
            redisCacheManager.set(ipAccessKey, ipAccess);

            redisCacheManager.rightPush(ipAccessTimeKey, accessTime);
            return super.preHandle(request, response, handler);
        }

        if (ipAccess.getFreezing() == 1) {
            if (ipAccess.getFreezingTime().plusNanos(freezingPeriod).isAfter(accessTime)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                logger.warn("To intercept a malicious request : {} , host ip is : {} , sessionId is : {}", url, host, sessionId);
                return false;
            } else {
                ipAccess.setFreezing(0);
                ipAccess.setFreezingTime(null);
                redisCacheManager.set(ipAccessKey, ipAccess);
            }
        }


        if (maxRecentAccessTimeNum <= redisCacheManager.lsize(ipAccessTimeKey)) {
            //如果达到最大访问次数

            LocalDateTime firstDate = (LocalDateTime) redisCacheManager.lrange(ipAccessTimeKey, 0, 1).get(0);
            LocalDateTime endDate = (LocalDateTime) redisCacheManager.lrange(ipAccessTimeKey, maxRecentAccessTimeNum - 1, maxRecentAccessTimeNum).get(0);

            if (firstDate.plusNanos(minRequestIntervalTime).isBefore(endDate)) {
                redisCacheManager.leftPop(ipAccessTimeKey);
                redisCacheManager.rightPush(ipAccessTimeKey, accessTime);
            } else {
                //如果时间差小于等于最小间隔，则可认为是恶意攻击

                if (ipAccess.getFreezing() == 0) {
                    ipAccess.setFreezing(1);
                    ipAccess.setFreezingTime(accessTime);
                    redisCacheManager.set(ipAccessKey, ipAccess);
                }

                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                logger.warn("To intercept a malicious request : {} , host ip is : {} , sessionId is : {}", url, host, sessionId);
                return false;
            }
        } else {
            redisCacheManager.rightPush(ipAccessTimeKey, accessTime);
        }

        return super.preHandle(request, response, handler);
    }
}
