package org.jaguar.modules.system.mgm.service;

import org.jaguar.core.base.BaseService;
import org.jaguar.modules.system.enums.MenuType;
import org.jaguar.modules.system.mgm.mapper.RoleMenuMapper;
import org.jaguar.modules.system.mgm.model.Menu;
import org.jaguar.modules.system.mgm.model.RoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private UserRoleService userRoleService;

    /**
     * 根据角色ID集合和菜单父ID，查询角色菜单，携带菜单信息返回
     *
     * @param parentId 如果parentId为null，则不列入查询条件；
     *                 如果parentId为0，则查询顶级菜单
     */
    public List<RoleMenu> listWithMenu(List<Long> roleIds, Long parentId, MenuType menuType) {
        return this.mapper.listWithMenu(roleIds, parentId, menuType);
    }

    /**
     * 根据用户ID查询用户菜单树
     */
    public List<Menu> treeMenuByUserId(Long currentUser) {
        List<Long> roleIds = userRoleService.listRoleIdsByUserId(currentUser);
        return this.treeMenu(roleIds, 0L);
    }

    /**
     * 根据角色ID集合和菜单父ID，递归查询用户菜单树
     */
    public List<Menu> treeMenu(List<Long> roleIds, Long parentId) {
        List<RoleMenu> roleMenuList = this.listWithMenu(roleIds, parentId, MenuType.MENU);

        List<Menu> menuList = new ArrayList<>();
        for (RoleMenu roleMenu : roleMenuList) {
            Menu menu = roleMenu.getMenu();
            List<Menu> children = this.treeMenu(roleIds, menu.getId());
            menu.setChildren(children);
            menuList.add(menu);
        }
        return menuList;
    }

    /**
     * 根据用户ID查询用户权限
     */
    public Set<String> listPermissionByUserId(Long currentUser) {
        Set<String> permissions = new HashSet<>();

        List<Long> roleIds = userRoleService.listRoleIdsByUserId(currentUser);
        List<RoleMenu> roleMenuList = this.listWithMenu(roleIds, null, null);
        for (RoleMenu roleMenu : roleMenuList) {
            switch (roleMenu.getMenu().getMenuType()) {
                case MENU: {
                    if (roleMenu.getRoleMenuPermission() != null) {
                        permissions.add(roleMenu.getMenu().getMenuAuthName() + ':' + roleMenu.getRoleMenuPermission());
                    }
                    break;
                }
                case FUNCTION: {
                    permissions.add(roleMenu.getMenu().getMenuAuthName());
                    break;
                }
                default:
            }
        }
        return permissions;
    }
}
