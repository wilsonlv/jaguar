package com.itqingning.jaguar.shiro;

import com.itqingning.jaguar.redis.RedisProperties;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * Created by lvws on 2019/4/18.
 */
@Configuration
@EnableConfigurationProperties({ShiroProperties.class, RedisProperties.class, ServerProperties.class})
public class ShiroConfig {

    @Bean
    public SimpleCookie simpleCookie(ShiroProperties shiroProperties) {
        return new SimpleCookie(shiroProperties.getCookieName());
    }

    @Bean
    public SessionListener sessionListener() {
        return new SessionListener();
    }

    @Bean
    public RedisManager redisManager(RedisProperties redisProperties, ServerProperties serverProperties) {
        Long seconds = serverProperties.getServlet().getSession().getTimeout().getSeconds();
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisProperties.getHost());
        redisManager.setPort(redisProperties.getPort());
        redisManager.setExpire(seconds.intValue());
        redisManager.setPassword(redisProperties.getPassword());
        return redisManager;
    }

    @Bean
    public RedisSessionDAO redisSessionDAO(RedisManager redisManager, RedisProperties redisProperties) {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setKeyPrefix(redisProperties.getNamespace() + ":" + redisSessionDAO.getKeyPrefix());
        redisSessionDAO.setRedisManager(redisManager);
        return redisSessionDAO;
    }

    @Bean
    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO, SimpleCookie simpleCookie,
                                         SessionListener sessionListener, ShiroProperties shiroProperties) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(shiroProperties.getSessionTimeoutMillis());
        sessionManager.setSessionDAO(redisSessionDAO);
        sessionManager.setSessionIdCookie(simpleCookie);
        sessionManager.getSessionListeners().add(sessionListener);
        return sessionManager;
    }

    @Bean
    public SecurityManager securityManager(SessionManager sessionManager, Realm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(sessionManager);
        securityManager.setRealm(realm);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager, ShiroProperties shiroProperties) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(shiroProperties.getLoginUrl());
        shiroFilterFactoryBean.setUnauthorizedUrl(shiroProperties.getUnauthorizedUrl());
        shiroFilterFactoryBean.setFilterChainDefinitions(shiroProperties.getFilterChainDefinitions());
        return shiroFilterFactoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator shiroProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }
}