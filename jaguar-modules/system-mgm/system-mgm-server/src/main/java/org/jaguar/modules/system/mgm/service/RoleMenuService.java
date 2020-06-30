package org.jaguar.modules.system.mgm.service;

import org.apache.commons.lang3.StringUtils;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.BaseService;
import org.jaguar.core.exception.Assert;
import org.jaguar.core.exception.CheckedException;
import org.jaguar.modules.system.mgm.mapper.RoleMenuMapper;
import org.jaguar.modules.system.mgm.model.Menu;
import org.jaguar.modules.system.mgm.model.Role;
import org.jaguar.modules.system.mgm.model.RoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统角色菜单表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Service
public class RoleMenuService extends BaseService<RoleMenu, RoleMenuMapper> {

    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;

    @Autowired
    private UserRoleService userRoleService;

    /*---------- 权限管理 ----------*/

    /**
     * 查询菜单树，并携带指定的角色权限返回
     *
     * @param roleId 角色ID
     * @return 带有角色权限的菜单树
     */
    @Transactional
    public List<Menu> treeMenuWithRolePermission(Long roleId) {
        List<Menu> menuTree = menuService.tree();
        for (Menu menu : menuTree) {
            RoleMenu roleMenu = this.unique(JaguarLambdaQueryWrapper.<RoleMenu>newInstance()
                    .eq(RoleMenu::getMenuId, menu.getId())
                    .eq(RoleMenu::getRoleId, roleId));
            menu.setRoleMenu(roleMenu);

            recursionChildrenRoleMenuPermission(menu, roleId);
        }
        return menuTree;
    }

    /**
     * 递归查询子菜单的角色权限
     *
     * @param parent 父菜单
     * @param roleId 角色ID
     */
    private void recursionChildrenRoleMenuPermission(Menu parent, Long roleId) {
        for (Menu child : parent.getChildren()) {
            RoleMenu roleMenu = this.unique(JaguarLambdaQueryWrapper.<RoleMenu>newInstance()
                    .eq(RoleMenu::getMenuId, child.getId())
                    .eq(RoleMenu::getRoleId, roleId));
            child.setRoleMenu(roleMenu);

            recursionChildrenRoleMenuPermission(child, roleId);
        }
    }

    @Transactional
    public RoleMenu change(RoleMenu roleMenu) {
        Role role = roleService.getById(roleMenu.getRoleId());
        Assert.validateId(role, "角色", roleMenu.getRoleId());

        Menu menu = menuService.getById(roleMenu.getMenuId());
        Assert.validateId(menu, "菜单", roleMenu.getMenuId());

        RoleMenu persistRoleMenu = this.unique(JaguarLambdaQueryWrapper.<RoleMenu>newInstance()
                .eq(RoleMenu::getRoleId, roleMenu.getRoleId())
                .eq(RoleMenu::getMenuId, roleMenu.getMenuId()));
        if (persistRoleMenu == null) {
            //在角色没有该菜单权限的情况下
            if (roleMenu.getRoleMenuPermission() == null) {
                throw new CheckedException("该角色目前没有该菜单权限！");
            } else {
                roleMenu = this.insert(roleMenu);
            }
        } else {
            if (roleMenu.getRoleMenuPermission() == null) {
                this.delete(persistRoleMenu.getId());
            } else {
                persistRoleMenu.setRoleMenuPermission(roleMenu.getRoleMenuPermission());
                roleMenu = this.updateById(persistRoleMenu);
            }
        }
        return roleMenu;
    }

    /*---------- 个人用户菜单权限查询 ----------*/

    /**
     * 递归查询"可查看"权限的子菜单
     *
     * @param parent  父菜单
     * @param roleIds 角色ID集合
     */
    private void recursionViewPermissionChildrenByRoleIds(Menu parent, List<Long> roleIds) {
        List<Menu> childrenMenuList = this.mapper.listViewMenusByRoleIdsAndParentId(roleIds, parent.getId());
        parent.setChildren(childrenMenuList);

        for (Menu children : childrenMenuList) {
            recursionViewPermissionChildrenByRoleIds(children, roleIds);
        }
    }

    /**
     * 根据用户ID查询"可查看"权限的用户菜单树
     */
    @Transactional
    public List<Menu> menuTreeViewPermissionByUserId(Long currentUser) {
        List<Long> roleIds = userRoleService.listRoleIdsByUserId(currentUser);
        if (roleIds.size() == 0) {
            return Collections.emptyList();
        }

        List<Menu> topMenuList = this.mapper.listViewMenusByRoleIdsAndParentId(roleIds, 0L);
        for (Menu menu : topMenuList) {
            recursionViewPermissionChildrenByRoleIds(menu, roleIds);
        }
        return topMenuList;
    }


    /**
     * 根据用户ID查询用户权限
     */
    public Set<String> listPermissionByUserId(Long currentUser) {
        Set<String> permissions = new HashSet<>();

        List<Long> roleIds = userRoleService.listRoleIdsByUserId(currentUser);
        List<RoleMenu> roleMenuList = this.mapper.listMaxPermissionsWithMenuByRoleIds(roleIds);
        for (RoleMenu roleMenu : roleMenuList) {
            Menu menu = roleMenu.getMenu();

            switch (menu.getMenuType()) {
                case MENU: {
                    if (StringUtils.isNotBlank(menu.getMenuAuthName()) && roleMenu.getRoleMenuPermission() != null) {
                        List<String> roleMenuPermissions = roleMenu.getRoleMenuPermission().permissions();
                        for (String roleMenuPermission : roleMenuPermissions) {
                            permissions.add(menu.getMenuAuthName() + ':' + roleMenuPermission);
                        }
                    }
                    break;
                }
                case FUNCTION: {
                    permissions.add(menu.getMenuAuthName());
                    break;
                }
                default:
            }
        }
        return permissions;
    }

}
