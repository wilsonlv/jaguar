package org.jaguar.modules.system.mgm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.jaguar.commons.data.encription.SecurityUtil;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.commons.utils.IdentifyingCode;
import org.jaguar.core.base.BaseService;
import org.jaguar.core.exception.Assert;
import org.jaguar.core.exception.CheckedException;
import org.jaguar.modules.system.mgm.mapper.UserMapper;
import org.jaguar.modules.system.mgm.model.User;
import org.jaguar.modules.system.mgm.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Service
public class UserService extends BaseService<User, UserMapper> {

    @Autowired
    private UserRoleService userRoleService;

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

    public User getByPrincipal(@NotBlank String principal) {
        return this.unique(JaguarLambdaQueryWrapper.<User>newInstance()
                .eq(User::getUserAccount, principal)
                .or().eq(User::getUserPhone, principal)
                .or().eq(User::getUserEmail, principal));
    }

    public User getDetail(Long currentUser) {
        User user = this.getById(currentUser);
        Assert.validateId(user, "用户", currentUser);

        List<UserRole> userRoleList = userRoleService.listWithRoleByUserId(currentUser);
        user.setUserRoleList(userRoleList);

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

        user.setUserPassword(newPassword);
        this.updateById(user);
    }


    /*----------  管理类接口  ----------*/

    @Transactional
    public Page<User> queryWithRoleAndDept(Page<User> page, LambdaQueryWrapper<User> wrapper) {
        Page<User> userPage = this.query(page, wrapper);
        for (User user : userPage.getRecords()) {
            List<UserRole> userRoleList = userRoleService.listWithRoleByUserId(user.getId());
            user.setUserRoleList(userRoleList);
        }
        return userPage;
    }

    /**
     * 新建用户
     */
    @Transactional
    public User create(User user) {
        User byAccount = this.getByAccount(user.getUserAccount());
        Assert.duplicate(byAccount, user, "登陆账号");

        if (StringUtils.isNotBlank(user.getUserPhone())) {
            User byPhone = this.getByPhone(user.getUserPhone());
            Assert.duplicate(byPhone, user, "手机号");
        }

        if (StringUtils.isNotBlank(user.getUserEmail())) {
            User byEmail = this.getByEmail(user.getUserEmail());
            Assert.duplicate(byEmail, user, "邮箱");
        }

        String password = IdentifyingCode.generate(12);
        user.setUserPassword(password);
        user = this.insert(user);

        user.setInitialPassword(password);
        return user;
    }

    /**
     * 修改用户信息
     */
    @Transactional
    public User modify(User user) {
        User persist = this.getById(user.getId());
        Assert.validateId(persist, "用户", user.getId());

        if (!persist.getUserAccount().equals(user.getUserAccount())) {
            throw new CheckedException("用户账号不可修改");
        }

        if (StringUtils.isNotBlank(user.getUserPhone())) {
            User byPhone = this.getByPhone(user.getUserPhone());
            Assert.duplicate(byPhone, user, "手机号");
        }

        if (StringUtils.isNotBlank(user.getUserEmail())) {
            User byEmail = this.getByEmail(user.getUserEmail());
            Assert.duplicate(byEmail, user, "邮箱");
        }

        user.setUserPassword(null);
        return this.updateById(user);
    }

    /**
     * 重置用户密码
     * <p>
     * return 新密码
     */
    @Transactional
    public String resetPassword(Long id) {
        User user = this.getById(id);
        Assert.validateId(user, "用户", id);

        String password = IdentifyingCode.generate(12);
        user.setUserPassword(password);

        this.updateById(user);
        return password;
    }

    @Transactional
    public Boolean toggleLock(Long id) {
        User user = this.getById(id);
        Assert.validateId(user, "用户", id);

        user.setUserLocked(!user.getUserLocked());
        user = this.updateById(user);
        return user.getUserLocked();
    }

}
