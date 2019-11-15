package org.jaguar.modules.system.mgm.service;

import org.jaguar.commons.data.encription.SecurityUtil;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.BaseService;
import org.jaguar.core.exception.CheckedException;
import org.jaguar.modules.system.mgm.mapper.UserMapper;
import org.jaguar.modules.system.mgm.model.User;
import org.jaguar.modules.system.mgm.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Service
@CacheConfig(cacheNames = "User")
public class UserService extends BaseService<User, UserMapper> {

    @Autowired
    private UserRoleService userRoleService;

    public User getByPrincipal(String principal) {
        return this.unique(JaguarLambdaQueryWrapper.<User>newInstance()
                .eq(User::getUserAccount, principal)
                .or().eq(User::getUserPhone, principal)
                .or().eq(User::getUserEmail, principal));
    }

    public User getPersonalInfo(Long currentUser) {
        User user = this.getById(currentUser);

        List<UserRole> userRoleList = userRoleService.listWithRoleByUserId(currentUser);
        user.setUserRoleList(userRoleList);

        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    public void modifyPassword(Long currentUser, String oldPassword, String newPassword) {
        User user = this.getById(currentUser);

        if (!user.getUserPassword().equals(oldPassword)) {
            throw new CheckedException("密码错误");
        }

        if (SecurityUtil.checkPassword(newPassword)) {
            throw new CheckedException("密码格式为包含数字，字母大小写的6-20位字符串！");
        }

        user.setUserPassword(newPassword);
        this.updateById(user);
    }


}
