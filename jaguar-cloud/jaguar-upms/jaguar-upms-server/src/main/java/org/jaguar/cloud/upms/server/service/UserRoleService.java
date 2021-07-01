package org.jaguar.cloud.upms.server.service;

import org.jaguar.cloud.upms.server.model.Role;
import org.jaguar.cloud.upms.server.model.User;
import org.jaguar.cloud.upms.server.model.UserRole;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.cloud.upms.server.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

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

    public List<Role> listRoleByUserId(@NotNull Long userId) {
        return this.mapper.listRoleByUserId(userId);
    }

    public Set<String> listMenuFunctionNamesByUserId(@NotNull Long userId) {
        return this.mapper.listMenuFunctionNamesByUserId(userId);
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
