package org.jaguar.modules.system.mgm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jaguar.commons.basecrud.Assert;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.commons.web.exception.CheckedException;
import org.jaguar.modules.system.mgm.mapper.RoleMapper;
import org.jaguar.modules.system.mgm.model.Role;
import org.jaguar.modules.system.mgm.model.RoleMenu;
import org.jaguar.modules.system.mgm.model.User;
import org.jaguar.modules.system.mgm.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Service
public class RoleService extends BaseService<Role, RoleMapper> {

    @Lazy
    @Autowired
    private UserRoleService userRoleService;
    @Lazy
    @Autowired
    private RoleMenuService roleMenuService;


    public Page<Role> queryWithUser(Page<Role> page, LambdaQueryWrapper<Role> wrapper) {
        page = this.query(page, wrapper);
        for (Role role : page.getRecords()) {
            List<User> users = userRoleService.listUserByRoleId(role.getId());
            role.setUsers(users);
        }
        return page;
    }

    public Role getDetail(Long roleId) {
        Role role = this.getById(roleId);
        Assert.validateId(role, "角色", roleId);

        List<RoleMenu> roleMenus = roleMenuService.list(JaguarLambdaQueryWrapper.<RoleMenu>newInstance()
                .eq(RoleMenu::getRoleId, roleId));
        role.setRoleMenus(roleMenus);

        return role;
    }

    @Transactional
    public Role createOrUpdate(Role role) {
        Role unique = this.unique(JaguarLambdaQueryWrapper.<Role>newInstance()
                .eq(Role::getRoleName, role));
        Assert.duplicate(unique, role, "角色名称");

        role = this.saveOrUpdate(role);

        roleMenuService.relateMenuFunctions(role.getId(), role.getMenuFunctionNames());

        return role;
    }

    @Transactional
    public void checkAnddelete(Long id) {
        if (userRoleService.exists(JaguarLambdaQueryWrapper.<UserRole>newInstance()
                .eq(UserRole::getRoleId, id))) {
            throw new CheckedException("该角色下已绑定用户，不可删除！");
        }

        this.delete(id);
    }

}
