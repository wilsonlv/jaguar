package org.jaguar.modules.system.mgm.service;

import org.jaguar.commons.basecrud.Assert;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.modules.system.mgm.mapper.UserRoleMapper;
import org.jaguar.modules.system.mgm.model.Role;
import org.jaguar.modules.system.mgm.model.User;
import org.jaguar.modules.system.mgm.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
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
    private RoleService roleService;

    public List<User> listUserByRoleId(@NotNull Long roleId) {
        return this.mapper.listUserByRoleId(roleId);
    }

    public List<Role> listRoleByUserId(Long userId) {
        return this.mapper.listRoleByUserId(userId);
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

    /**
     * 用户关联角色
     *
     * @param userId  用户ID
     * @param roleIds 角色ID集合
     */
    @Transactional
    public void relateRoles(Long userId, List<Long> roleIds) {
        for (Long roleId : roleIds) {
            Role role = roleService.getById(roleId);
            Assert.validateId(role, "角色", roleId);

            if (this.exists(JaguarLambdaQueryWrapper.<UserRole>newInstance()
                    .eq(UserRole::getUserId, userId)
                    .eq(UserRole::getRoleId, roleId))) {
                continue;
            }

            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            this.insert(userRole);
        }

        this.delete(JaguarLambdaQueryWrapper.<UserRole>newInstance()
                .eq(UserRole::getUserId, userId)
                .notIn(UserRole::getRoleId, roleIds));
    }
}
