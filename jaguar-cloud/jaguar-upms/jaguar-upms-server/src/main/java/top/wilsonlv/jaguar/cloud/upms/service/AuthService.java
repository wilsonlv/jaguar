package top.wilsonlv.jaguar.cloud.upms.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wilsonlv.jaguar.cloud.upms.entity.Menu;
import top.wilsonlv.jaguar.cloud.upms.entity.RoleMenu;
import top.wilsonlv.jaguar.cloud.upms.entity.User;
import top.wilsonlv.jaguar.cloud.upms.entity.UserRole;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.MenuVO;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
import top.wilsonlv.jaguar.commons.basecrud.BaseModel;
import top.wilsonlv.jaguar.commons.data.encryption.util.EncryptionUtil;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;

import java.time.LocalDateTime;
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

    private final UserService userService;

    private final UserRoleService userRoleService;

    private final RoleMenuService roleMenuService;

    private final MenuService menuService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserVO getByPrincipalWithRoleAndPermission(String username) {
        User user = userService.unique(Wrappers.lambdaQuery(User.class)
                .select(BaseModel::getId)
                .eq(User::getUserAccount, username).or()
                .eq(User::getUserPhone, username).or()
                .eq(User::getUserEmail, username));
        if (user == null) {
            return null;
        }

        return userService.getDetail(user.getId());
    }

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

    @Transactional
    public void modifyUserInfo(Long userId, String userPhone, String userEmail, String userNickName) {
        User user = null;
        user.setId(userId);
        user.setUserPhone(userPhone);
        user.setUserEmail(userEmail);
        user.setUserNickName(userNickName);
        userService.updateById(user);
    }

    @Transactional
    public void modifyPassword(Long userId, String oldPassword, String newPassword) {
        User user = userService.getById(userId);

        if (!passwordEncoder.matches(oldPassword, user.getUserPassword())) {
            throw new CheckedException("密码错误");
        }

        if (EncryptionUtil.passwordUnmatched(newPassword)) {
            throw new CheckedException("密码格式为包含数字，字母大小写的6-20位字符串！");
        }

        user = new User();
        user.setId(userId);
        user.setUserPassword(newPassword);
        user.setUserPasswordLastModifyTime(LocalDateTime.now());
        userService.updateById(user);
    }
}
