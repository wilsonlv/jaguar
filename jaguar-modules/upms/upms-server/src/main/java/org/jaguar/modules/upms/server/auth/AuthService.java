package org.jaguar.modules.upms.server.auth;


import org.apache.commons.lang3.StringUtils;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.commons.data.encription.SecurityUtil;
import org.jaguar.commons.web.exception.CheckedException;
import org.jaguar.modules.upms.dto.MenuFunction;
import org.jaguar.modules.upms.server.mapper.UserMapper;
import org.jaguar.modules.upms.server.model.Role;
import org.jaguar.modules.upms.server.model.User;
import org.jaguar.modules.upms.server.service.UserRoleService;
import org.jaguar.modules.upms.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/4/8
 */
@Service
public class AuthService extends BaseService<User, UserMapper> implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;

    @Transactional
    public User getDetail(User user) {
        //获取角色
        List<Role> roles = userRoleService.listRoleByUserId(user.getId());
        user.setRoles(roles);

        //获取菜单功能
        Set<String> menuFunctionNames = userRoleService.listMenuFunctionNamesByUserId(user.getId());
        user.setMenuFunctions(MenuFunction.filterMenuFunctions(menuFunctionNames));

        //获取权限
        for (String menuFunctionName : menuFunctionNames) {
            MenuFunction menuFunction = MenuFunction.getMenuFunction(menuFunctionName);
            if (menuFunction != null && StringUtils.isNotBlank(menuFunction.getPermission())) {
                user.getAuthorities().add(new SimpleGrantedAuthority(menuFunction.getPermission()));
            }
        }

        return user;
    }

    @Transactional
    public void modifyPassword(Long currentUser, String oldPassword, String newPassword) {
        User user = this.getById(currentUser);

        if (!user.getUserPassword().equals(oldPassword)) {
            throw new CheckedException("密码错误");
        }

        if (!SecurityUtil.checkPassword(newPassword)) {
            throw new CheckedException("密码格式为包含数字，字母大小写的6-20位字符串！");
        }

        user = new User();
        user.setId(currentUser);
        user.setUserPassword(newPassword);
        this.updateById(user);
    }

    /**
     * security 登录时调用
     *
     * @param username 用户账号
     * @return 用户
     * @throws UsernameNotFoundException 用户名和密码错误
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByAccount(username);
        if (user == null) {
            throw new UsernameNotFoundException(null);
        }

        return this.getDetail(user);
    }
}
