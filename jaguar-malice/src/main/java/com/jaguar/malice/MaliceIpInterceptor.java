package com.jaguar.malice;

import com.jaguar.core.http.HttpCode;
import com.jaguar.redis.RedisCacheManager;
import com.jaguar.web.WebUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by lvws on 2018/11/9.
 */
@Component
public class MaliceIpInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LogManager.getLogger(getClass());

    private static final String IP_ACCESS_PREFIX = "SysIpAccess:";

    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private MaliceProperties maliceProperties;

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
        String host = WebUtil.getHost(request);

        Long freezingPeriod = maliceProperties.getIpFreezingPeriod() * 1000L;
        Integer maxRecentAccessTimeNum = maliceProperties.getIpMaxRecentAccessTimeNum();
        Long minRequestIntervalTime = maliceProperties.getIpMinRequestIntervalTime() * 1000L;

        Date accessTime = new Date();

        String ipAccessKey = getIpAccessKey(host);
        String ipAccessTimeKey = getIpAccessTimeKey(host);

        SysIpAccess sysIpAccess = (SysIpAccess) redisCacheManager.get(ipAccessKey);
        if (sysIpAccess == null) {
            sysIpAccess = new SysIpAccess();
            sysIpAccess.setAddress(host);
            sysIpAccess.setFreezing(0);
            redisCacheManager.set(ipAccessKey, sysIpAccess);

            redisCacheManager.rightPush(ipAccessTimeKey, accessTime);
            return super.preHandle(request, response, handler);
        }

        if (sysIpAccess.getFreezing() == 1) {
            if ((sysIpAccess.getFreezingTime().getTime() + freezingPeriod * 1000) > accessTime.getTime()) {
                response.setStatus(HttpCode.MULTI_STATUS.value());
                logger.warn("To intercept a malicious request : {} , host ip is : {} , sessionId is : {}", url, host, sessionId);
                return false;
            } else {
                sysIpAccess.setFreezing(0);
                sysIpAccess.setFreezingTime(null);
                redisCacheManager.set(ipAccessKey, sysIpAccess);
            }
        }

        if (maxRecentAccessTimeNum <= redisCacheManager.lsize(ipAccessTimeKey)) {//如果达到最大访问次数
            Date firstDate = (Date) redisCacheManager.lrange(ipAccessTimeKey, 0, 1).get(0);
            Date endDate = (Date) redisCacheManager.lrange(ipAccessTimeKey, maxRecentAccessTimeNum - 1, maxRecentAccessTimeNum).get(0);
            if ((endDate.getTime() - firstDate.getTime()) / 1000 > minRequestIntervalTime) {
                redisCacheManager.leftPop(ipAccessTimeKey);
                redisCacheManager.rightPush(ipAccessTimeKey, accessTime);
            } else {//如果时间差小于等于最小间隔，则可认为是恶意攻击
                if (sysIpAccess.getFreezing() == 0) {
                    sysIpAccess.setFreezing(1);
                    sysIpAccess.setFreezingTime(accessTime);
                    redisCacheManager.set(ipAccessKey, sysIpAccess);
                }

                response.setStatus(HttpCode.MULTI_STATUS.value());
                logger.warn("To intercept a malicious request : {} , host ip is : {} , sessionId is : {}", url, host, sessionId);
                return false;
            }
        } else {
            redisCacheManager.rightPush(ipAccessTimeKey, accessTime);
        }

        return super.preHandle(request, response, handler);
    }
}
