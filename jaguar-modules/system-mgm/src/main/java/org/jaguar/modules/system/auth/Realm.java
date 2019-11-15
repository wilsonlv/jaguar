package org.jaguar.modules.system.auth;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.jaguar.core.exception.CheckedException;
import org.jaguar.core.web.LoginUtil;
import org.jaguar.modules.handlerlog.intercepter.HandlerLogInterceptor;
import org.jaguar.modules.handlerlog.model.HandlerLog;
import org.jaguar.modules.system.mgm.model.Login;
import org.jaguar.modules.system.mgm.model.RoleMenu;
import org.jaguar.modules.system.mgm.model.User;
import org.jaguar.modules.system.mgm.model.UserRole;
import org.jaguar.modules.system.mgm.service.RoleMenuService;
import org.jaguar.modules.system.mgm.service.UserRoleService;
import org.jaguar.modules.system.mgm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author lvws
 * @since 2019-11-05
 */
@Slf4j
@Service
public class Realm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleMenuService roleMenuService;


    @Override
    public Class getAuthenticationTokenClass() {
        return Login.class;
    }

    @Override
    public void setAuthorizationCachingEnabled(@Value("false") boolean authenticationCachingEnabled) {
        super.setAuthorizationCachingEnabled(authenticationCachingEnabled);
    }

    /**
     * 接口权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return LoginUtil.getCurrentUserAuthInfo();
    }

    /**
     * 登录验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        Login token = (Login) authenticationToken;

        User user = userService.getByPrincipal(token.getPrincipal());
        if (user == null || !user.getUserPassword().equals(token.getCredentials())) {
            throw new CheckedException("用户名或密码错误");
        }

        if (user.getUserLocked()) {
            throw new CheckedException("用户账号已被锁定");
        }

        //登陆成功
        LoginUtil.saveCurrentUser(user.getId());
        LoginUtil.saveCurrentUserAccount(user.getUserAccount());

        HandlerLog handlerLog = HandlerLogInterceptor.HANDLER_LOG.get();
        handlerLog.setCreateBy(user.getId());

        //查询用户权限
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> permissions = roleMenuService.listPermissionByUserId(user.getId());
        info.addStringPermissions(permissions);
        LoginUtil.saveCurrentUserAuthInfo(info);

        return new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), user.getUserAccount());
    }
}
