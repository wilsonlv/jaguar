package com.itqingning.jaguar.interceptor;

import com.itqingning.jaguar.core.http.HttpCode;
import com.itqingning.jaguar.web.WebUtil;
import com.itqingning.jaguar.redis.RedisCacheManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${interceptor.malice.namespace}")
    private String namespace;

    /**
     * 冻结期（s）
     */
    @Value("${interceptor.malice.ip.freezingPeriod}")
    private Long freezingPeriod = 600L;

    /**
     * 访问maxRecentAccessTimeNum次的最小访问时间间隔（s）
     */
    @Value("${interceptor.malice.ip.minRequestIntervalTime}")
    private Long minRequestIntervalTime;

    /**
     * 最多记录最近XX次的访问时间记录
     */
    @Value("${interceptor.malice.ip.maxRecentAccessTimeNum}")
    private Long maxRecentAccessTimeNum;


    private String getIpAccessKey(String host) {
        return new StringBuilder(namespace).append(":").append(IP_ACCESS_PREFIX).append(host).toString();
    }

    private String getIpAccessTimeKey(String host) {
        return new StringBuilder(namespace).append(":").append(IP_ACCESS_PREFIX).append(host).append(":times").toString();
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


    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setRedisCacheManager(RedisCacheManager redisCacheManager) {
        this.redisCacheManager = redisCacheManager;
    }

    public void setFreezingPeriod(Long freezingPeriod) {
        this.freezingPeriod = freezingPeriod;
    }

    public void setMinRequestIntervalTime(Long minRequestIntervalTime) {
        this.minRequestIntervalTime = minRequestIntervalTime;
    }

    public void setMaxRecentAccessTimeNum(Long maxRecentAccessTimeNum) {
        this.maxRecentAccessTimeNum = maxRecentAccessTimeNum;
    }
}
