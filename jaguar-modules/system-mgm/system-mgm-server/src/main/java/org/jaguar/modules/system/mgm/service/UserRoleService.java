package org.jaguar.modules.system.mgm.service;

import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.BaseService;
import org.jaguar.core.exception.Assert;
import org.jaguar.core.exception.CheckedException;
import org.jaguar.modules.system.mgm.mapper.UserRoleMapper;
import org.jaguar.modules.system.mgm.model.Role;
import org.jaguar.modules.system.mgm.model.User;
import org.jaguar.modules.system.mgm.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统用户角色表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-11-15
 */
@Service
public class UserRoleService extends BaseService<UserRole, UserRoleMapper> {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    public List<UserRole> listWithUserByRoleId(Long roleId) {
        return this.mapper.listWithUserByRoleId(roleId);
    }

    public List<UserRole> listWithRoleByUserId(Long userId) {
        return this.mapper.listWithRoleByUserId(userId);
    }

    public List<Long> listRoleIdsByUserId(Long userId) {
        List<UserRole> userRoleList = this.list(JaguarLambdaQueryWrapper.<UserRole>newInstance()
                .eq(UserRole::getUserId, userId)
                .select(UserRole::getRoleId));

        List<Long> roleIds = new ArrayList<>();
        for (UserRole userRole : userRoleList) {
            roleIds.add(userRole.getRoleId());
        }
        return roleIds;
    }

    @Transactional
    public UserRole create(UserRole userRole) {
        User user = userService.getById(userRole.getUserId());
        Assert.validateId(user, "用户", userRole.getUserId());

        Role role = roleService.getById(userRole.getRoleId());
        Assert.validateId(role, "角色", userRole.getRoleId());

        if (this.exists(JaguarLambdaQueryWrapper.<UserRole>newInstance()
                .eq(UserRole::getUserId, userRole.getUserId())
                .eq(UserRole::getRoleId, userRole.getRoleId()))) {
            throw new CheckedException("该用户角色已存在！");
        }
        return this.saveOrUpdate(userRole);
    }
}
