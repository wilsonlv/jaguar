package org.jaguar.core.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.session.Session;
import org.jaguar.core.Constant;

/**
 * Created by lvws on 2019/5/23.
 */
public final class LoginUtil {

    private LoginUtil() {
    }

    private static Logger logger = LogManager.getLogger();

    /**
     * 保存当前用户
     */
    public static void saveCurrentUser(Long user) {
        setSession(Constant.CURRENT_USER, user);
    }

    /**
     * 保存当前用户
     */
    public static void saveCurrentUserAccount(String userAccount) {
        setSession(Constant.CURRENT_USER_ACCOUNT, userAccount);
    }

    /**
     * 保存当前用户权限
     */
    public static void saveCurrentUserAuthInfo(AuthorizationInfo userAuthInfo) {
        setSession(Constant.CURRENT_USER_AUTH_INFO, userAuthInfo);
    }

    public static Long getCurrentUser() {
        return (Long) getSession().getAttribute(Constant.CURRENT_USER);
    }

    public static String getCurrentUserAccount() {
        return (String) getSession().getAttribute(Constant.CURRENT_USER_ACCOUNT);
    }

    public static AuthorizationInfo getCurrentUserAuthInfo() {
        return (AuthorizationInfo) getSession().getAttribute(Constant.CURRENT_USER_AUTH_INFO);
    }

    /**
     * 将一些数据放到ShiroSession中,以便于其它地方使用
     */
    public static void setSession(Object key, Object value) {
        SecurityUtils.getSubject().getSession().setAttribute(key, value);
    }

    /**
     * 获取session
     */
    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

}
