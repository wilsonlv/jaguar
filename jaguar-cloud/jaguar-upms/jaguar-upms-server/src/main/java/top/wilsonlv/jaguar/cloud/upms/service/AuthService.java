package top.wilsonlv.jaguar.cloud.upms.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wilsonlv.jaguar.cloud.upms.entity.Menu;
import top.wilsonlv.jaguar.cloud.upms.entity.RoleMenu;
import top.wilsonlv.jaguar.cloud.upms.entity.UserRole;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.MenuVO;
import top.wilsonlv.jaguar.commons.basecrud.BaseModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lvws
 * @since 2021/11/1
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRoleService userRoleService;

    private final RoleMenuService roleMenuService;

    private final MenuService menuService;

    @Transactional
    public List<MenuVO> getUserMenus(Long currentUserId) {
        List<UserRole> userRoles = userRoleService.list(Wrappers.lambdaQuery(UserRole.class)
                .eq(UserRole::getUserId, currentUserId));
        Set<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet());

        List<RoleMenu> roleMenus = roleMenuService.list(Wrappers.lambdaQuery(RoleMenu.class).in(RoleMenu::getRoleId, roleIds));
        Set<Long> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toSet());

        List<Menu> menus = menuService.list(Wrappers.lambdaQuery(Menu.class)
                .in(BaseModel::getId, menuIds)
                .orderByAsc(Menu::getMenuOrder));
        return list2tree(menus);
    }

    private List<MenuVO> list2tree(List<Menu> menus) {
        return findMenuByParentId(null, menus);
    }

    private List<MenuVO> findMenuByParentId(Long parentId, List<Menu> menus) {
        List<MenuVO> menuVOs = new ArrayList<>();
        Iterator<Menu> iterator = menus.iterator();
        while (iterator.hasNext()) {
            Menu menu = iterator.next();
            if ((parentId == null && menu.getParentId() == null) ||
                    (parentId != null && parentId.equals(menu.getParentId()))) {
                iterator.remove();
                MenuVO userMenuVO = menu.toVo(MenuVO.class);
                menuVOs.add(userMenuVO);
            }
        }

        for (MenuVO menuVO : menuVOs) {
            List<MenuVO> children = findMenuByParentId(menuVO.getId(), menus);
            menuVO.setChildren(children);
        }

        return menuVOs;
    }

}
