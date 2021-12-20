package top.wilsonlv.jaguar.cloud.upms.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wilsonlv.jaguar.basecrud.Assert;
import top.wilsonlv.jaguar.basecrud.BaseModel;
import top.wilsonlv.jaguar.cloud.upms.entity.*;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.MenuVO;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.ResourceServerVO;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
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
public class PersonalService {

    private final UserService userService;

    private final UserRoleService userRoleService;

    private final RoleMenuService roleMenuService;

    private final MenuService menuService;

    private final ResourceServerService resourceServerService;

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
    public List<MenuVO> getUserMenus(String serverId, Long currentUserId) {
        List<UserRole> userRoles = userRoleService.list(Wrappers.lambdaQuery(UserRole.class)
                .eq(UserRole::getUserId, currentUserId));
        Set<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet());

        List<RoleMenu> roleMenus = roleMenuService.list(Wrappers.lambdaQuery(RoleMenu.class)
                .in(RoleMenu::getRoleId, roleIds));
        Set<Long> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toSet());

        ResourceServer resourceServer = resourceServerService.getByServerId(serverId);
        List<Menu> menus = menuService.list(Wrappers.lambdaQuery(Menu.class)
                .eq(Menu::getResourceServerId, resourceServer.getId())
                .in(BaseModel::getId, menuIds)
                .orderByAsc(Menu::getMenuOrder));
        return list2tree(0, menus);
    }

    private List<MenuVO> list2tree(long parentId, List<Menu> menus) {
        List<MenuVO> menuVos = new ArrayList<>();
        Iterator<Menu> iterator = menus.iterator();
        while (iterator.hasNext()) {
            Menu menu = iterator.next();
            if (menu.getParentId().equals(parentId)) {
                iterator.remove();
                MenuVO userMenuVO = menu.toVo(MenuVO.class);
                menuVos.add(userMenuVO);
            }
        }

        for (MenuVO menuVO : menuVos) {
            List<MenuVO> children = list2tree(menuVO.getId(), menus);
            menuVO.setChildren(children);
        }
        return menuVos;
    }

    @Transactional
    public List<ResourceServerVO> getUserResourceServers(Long currentUserId) {
        List<ResourceServer> resourceServers = resourceServerService.list(Wrappers.lambdaQuery(ResourceServer.class)
                .eq(ResourceServer::getServerMenu, true));

        List<ResourceServerVO> resourceServerVos = new ArrayList<>(resourceServers.size());
        for (ResourceServer resourceServer : resourceServers) {
            ResourceServerVO resourceServerVO = resourceServer.toVo(ResourceServerVO.class);
            resourceServerVO.setServerSecret(null);
            resourceServerVos.add(resourceServerVO);
        }
        return resourceServerVos;
    }

    @Transactional
    public void modifyUserInfo(Long userId, String userPhone, String userEmail, String userNickName) {
        User user = new User();
        user.setId(userId);
        user.setUserPhone(userPhone);
        user.setUserEmail(userEmail);
        user.setUserNickName(userNickName);

        if (StringUtils.hasText(userPhone)) {
            User byPhone = userService.getByPhone(userPhone);
            Assert.duplicate(byPhone, user, "用户手机号");
        }

        if (StringUtils.hasText(userEmail)) {
            User byEmail = userService.getByEmail(userEmail);
            Assert.duplicate(byEmail, user, "用户邮箱");
        }

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
        user.setUserPassword(passwordEncoder.encode(newPassword));
        user.setUserPasswordLastModifyTime(LocalDateTime.now());
        userService.updateById(user);
    }


}
