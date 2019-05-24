package org.jaguar.core.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
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
     * 获取当前用户
     */
    public static Long getCurrentUser() {
        try {
            return (Long) SecurityUtils.getSubject().getSession().getAttribute(Constant.CURRENT_USER);
        } catch (UnavailableSecurityManagerException e) {
            return null;
        }
    }

    public static String getCurrentUserAccount() {
        try {
            return (String) SecurityUtils.getSubject().getSession().getAttribute(Constant.CURRENT_USER_ACCOUNT);
        } catch (UnavailableSecurityManagerException e) {
            return null;
        }
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
        try {
            return SecurityUtils.getSubject().getSession();
        } catch (UnavailableSecurityManagerException e) {
            return null;
        }
    }

}
