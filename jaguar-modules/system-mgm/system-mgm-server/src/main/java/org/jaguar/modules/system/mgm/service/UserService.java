package org.jaguar.modules.system.mgm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.jaguar.commons.basecrud.Assert;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.commons.data.encription.SecurityUtil;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.commons.web.exception.CheckedException;
import org.jaguar.modules.system.mgm.dto.MenuFunction;
import org.jaguar.modules.system.mgm.mapper.UserMapper;
import org.jaguar.modules.system.mgm.model.Role;
import org.jaguar.modules.system.mgm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Service
public class UserService extends BaseService<User, UserMapper> implements UserDetailsService {

    @Lazy
    @Autowired
    private UserRoleService userRoleService;

    /*----------  通用接口  ----------*/

    public User getByAccount(@NotBlank String account) {
        return this.unique(JaguarLambdaQueryWrapper.<User>newInstance()
                .eq(User::getUserAccount, account));
    }

    public User getByPhone(@NotBlank String phone) {
        return this.unique(JaguarLambdaQueryWrapper.<User>newInstance()
                .eq(User::getUserPhone, phone));
    }

    public User getByEmail(@NotBlank String email) {
        return this.unique(JaguarLambdaQueryWrapper.<User>newInstance()
                .eq(User::getUserEmail, email));
    }

    /**
     * 获取用户详情
     *
     * @param currentUser                     用户ID
     * @param withMenuFunctionsAndAuthorities 是否携带菜单功能和权限返回
     * @return 用户详情
     */
    @Transactional
    public User getDetail(Long currentUser, boolean withMenuFunctionsAndAuthorities) {
        User user = this.getById(currentUser);
        Assert.validateId(user, "用户", currentUser);

        return this.getDetail(user, withMenuFunctionsAndAuthorities);
    }

    @Transactional
    public User getDetail(User user, boolean withMenuFunctionsAndAuthorities) {
        //获取角色
        List<Role> roles = userRoleService.listRoleByUserId(user.getId());
        user.setRoles(roles);

        if (withMenuFunctionsAndAuthorities) {
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
        }
        return user;
    }


    /*----------  个人用户接口  ----------*/

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
        User user = this.getByAccount(username);
        if (user == null) {
            throw new UsernameNotFoundException(null);
        }

        return getDetail(user.getId(), true);
    }

    /*----------  管理类接口  ----------*/

    @Transactional
    public Page<User> queryWithRole(Page<User> page, LambdaQueryWrapper<User> wrapper) {
        page = this.query(page, wrapper);
        for (User user : page.getRecords()) {
            List<Role> userRoleList = userRoleService.listRoleByUserId(user.getId());
            user.setRoles(userRoleList);
        }
        return page;
    }

    /**
     * 新建用户
     */
    @Transactional
    public User create(User user) {
        Assert.notNull(user.getUserPassword(), "用户密码");
        if (!SecurityUtil.checkPassword(user.getUserPassword())) {
            throw new CheckedException("密码格式为包含数字，字母大小写的6-20位字符串！");
        }

        User byAccount = this.getByAccount(user.getUserAccount());
        Assert.duplicate(byAccount, user, "登录账号");

        if (StringUtils.isNotBlank(user.getUserPhone())) {
            User byPhone = this.getByPhone(user.getUserPhone());
            Assert.duplicate(byPhone, user, "手机号");
        }

        if (StringUtils.isNotBlank(user.getUserEmail())) {
            User byEmail = this.getByEmail(user.getUserEmail());
            Assert.duplicate(byEmail, user, "邮箱");
        }

        user.setUserLocked(false);
        user = this.insert(user);

        userRoleService.relateRoles(user.getId(), user.getRoleIds());
        return user;
    }

    /**
     * 修改用户信息
     */
    @Transactional
    public User modify(User user) {
        User persist = this.getById(user.getId());
        Assert.validateId(persist, "用户", user.getId());

        if (StringUtils.isNotBlank(user.getUserPhone())) {
            User byPhone = this.getByPhone(user.getUserPhone());
            Assert.duplicate(byPhone, user, "手机号");
        }

        if (StringUtils.isNotBlank(user.getUserEmail())) {
            User byEmail = this.getByEmail(user.getUserEmail());
            Assert.duplicate(byEmail, user, "邮箱");
        }

        user.setUserAccount(null);
        user.setUserPassword(null);
        user.setUserLocked(null);
        user = this.updateById(user);

        userRoleService.relateRoles(user.getId(), user.getRoleIds());
        return user;
    }

}
