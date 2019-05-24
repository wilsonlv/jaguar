package org.jaguar.commons.shiro.config;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lvws on 2019/4/18.
 */
@Configuration
public class ShiroConfig {

    @Autowired
    private ShiroProperties shiroProperties;

    public static final String ANON = "anon";
    public static final String AUTHC = "authc";

    @Bean
    public DefaultWebSecurityManager securityManager(SessionManager sessionManager, RedisCacheManager shiroCacheMananger, Realm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(shiroCacheMananger);
        securityManager.setRealm(realm);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(shiroProperties.getLoginUrl());
        shiroFilterFactoryBean.setUnauthorizedUrl(shiroProperties.getUnauthorizedUrl());

        Map<String, String> filterChainDefinitionMap = new HashMap<>();
        String[] anons = shiroProperties.getFilterChainDefinitions().split(",");
        for (String anon : anons) {
            filterChainDefinitionMap.put(anon, ANON);
        }
        filterChainDefinitionMap.put("/**", AUTHC);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
