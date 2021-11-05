package top.wilsonlv.jaguar.cloud.upms.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wilsonlv.jaguar.cloud.upms.entity.Menu;
import top.wilsonlv.jaguar.cloud.upms.entity.RoleMenu;
import top.wilsonlv.jaguar.cloud.upms.mapper.RoleMenuMapper;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;

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
@RequiredArgsConstructor
public class RoleMenuService extends BaseService<RoleMenu, RoleMenuMapper> {

    private final MenuService menuService;

    /*----------  通用接口  ----------*/

    public Set<String> listPermissionsByRoleIds(Set<Long> roleIds) {
        return this.mapper.listPermissionsByRoleIds(roleIds);
    }

    /*---------- 权限管理 ----------*/

    @Transactional
    public void relateMenus(Long roleId, Set<Long> menuIds) {
        for (Long menuId : menuIds) {
            if (this.exists(Wrappers.lambdaQuery(RoleMenu.class)
                    .eq(RoleMenu::getRoleId, roleId)
                    .eq(RoleMenu::getMenuId, menuId))) {
                continue;
            }

            Menu menu = menuService.getById(menuId);

            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menu.getId());
            roleMenu.setBuiltIn(false);
            this.insert(roleMenu);
        }

        this.delete(Wrappers.lambdaQuery(RoleMenu.class)
                .eq(RoleMenu::getRoleId, roleId)
                .eq(RoleMenu::getBuiltIn, false)
                .notIn(RoleMenu::getMenuId, menuIds));
    }

}
