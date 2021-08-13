package top.wilsonlv.jaguar.cloud.upms.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.wilsonlv.jaguar.cloud.upms.model.Role;
import top.wilsonlv.jaguar.cloud.upms.model.RoleMenu;
import top.wilsonlv.jaguar.cloud.upms.model.User;
import top.wilsonlv.jaguar.cloud.upms.model.UserRole;
import top.wilsonlv.jaguar.commons.basecrud.Assert;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;
import top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;
import top.wilsonlv.jaguar.cloud.upms.mapper.RoleMapper;
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
//            role.setUsers(users);
        }
        return page;
    }

    public Role getDetail(Long roleId) {
        Role role = this.getById(roleId);

        List<RoleMenu> roleMenus = roleMenuService.list(JaguarLambdaQueryWrapper.<RoleMenu>newInstance()
                .eq(RoleMenu::getRoleId, roleId));
//        role.setRoleMenus(roleMenus);

        return role;
    }

    @Transactional
    public void createOrUpdate(Role role) {
        Role unique = this.unique(JaguarLambdaQueryWrapper.<Role>newInstance()
                .eq(Role::getRoleName, role));
        Assert.duplicate(unique, role, "角色名称");

        this.saveOrUpdate(role);

//        roleMenuService.relateMenuFunctions(role.getId(), role.getMenuFunctionNames());
    }

    @Transactional
    public void checkAndDelete(Long id) {
        if (userRoleService.exists(JaguarLambdaQueryWrapper.<UserRole>newInstance()
                .eq(UserRole::getRoleId, id))) {
            throw new CheckedException("该角色下已绑定用户，不可删除！");
        }

        this.delete(id);
    }

}
