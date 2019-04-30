package com.jaguar.shiro;


import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lvws on 2019/4/18.
 */
@Configuration
public class ShiroConfig {

    @Autowired
    private ShiroProperties shiroProperties;

    @Bean
    public DefaultWebSecurityManager securityManager(SessionManager sessionManager, RedisCacheManager redisCacheManager, Realm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(redisCacheManager);
        securityManager.setRealm(realm);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(shiroProperties.getLoginUrl());
        shiroFilterFactoryBean.setUnauthorizedUrl(shiroProperties.getUnauthorizedUrl());
        shiroFilterFactoryBean.setFilterChainDefinitions(shiroProperties.getFilterChainDefinitions());
        return shiroFilterFactoryBean;
    }

    @Bean
    @ConditionalOnMissingBean(Realm.class)
    public Realm realm() {
        return new SimpleAccountRealm();
    }

}