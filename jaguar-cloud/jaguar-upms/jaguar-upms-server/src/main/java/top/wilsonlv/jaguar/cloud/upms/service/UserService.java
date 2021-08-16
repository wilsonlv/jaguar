package top.wilsonlv.jaguar.cloud.upms.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wilsonlv.jaguar.cloud.upms.mapper.UserMapper;
import top.wilsonlv.jaguar.cloud.upms.model.Role;
import top.wilsonlv.jaguar.cloud.upms.model.User;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.RoleVO;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
import top.wilsonlv.jaguar.commons.basecrud.Assert;
import top.wilsonlv.jaguar.commons.basecrud.BaseModel;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;
import top.wilsonlv.jaguar.commons.data.encryption.util.EncryptionUtil;
import top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
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
public class UserService extends BaseService<User, UserMapper> {

    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleMenuService roleMenuService;


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

    public UserVO getByPrincipalWithRoleAndPermission(String username) {
        User user = this.unique(Wrappers.lambdaQuery(User.class)
                .select(BaseModel::getId)
                .eq(User::getUserAccount, username).or()
                .eq(User::getUserPhone, username).or()
                .eq(User::getUserEmail, username));
        if (user == null) {
            return null;
        }

        return this.getDetail(user.getId());
    }

    /**
     * 获取用户详情
     *
     * @param currentUser 用户ID
     * @return 用户详情
     */
    @Transactional
    public UserVO getDetail(Long currentUser) {
        User user = this.getById(currentUser);

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        List<Role> roles = userRoleService.listRoleByUserId(user.getId());
        userVO.setRoles(new ArrayList<>(roles.size()));

        Set<Long> roleIds = new HashSet<>();
        for (Role role : roles) {
            RoleVO roleVO = new RoleVO(role.getRoleName(), role.getRoleBuiltIn(), role.getRoleEnable());
            userVO.getRoles().add(roleVO);

            roleIds.add(role.getId());
        }

        Set<String> permissions = roleMenuService.listPermissionsByRoleIds(roleIds);
        userVO.setPermissions(permissions);

        return userVO;
    }

    /*----------  管理类接口  ----------*/

    @Transactional
    public Page<User> queryWithRole(Page<User> page, LambdaQueryWrapper<User> wrapper) {
        page = this.query(page, wrapper);
        for (User user : page.getRecords()) {
            List<Role> userRoleList = userRoleService.listRoleByUserId(user.getId());
//            user.setRoles(userRoleList);
        }
        return page;
    }

    /**
     * 新建用户
     */
    @Transactional
    public void create(User user) {
        Assert.notNull(user.getUserPassword(), "用户密码");
        if (EncryptionUtil.passwordUnmatched(user.getUserPassword())) {
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
        this.insert(user);

//        userRoleService.relateRoles(user.getId(), user.getRoleIds());
    }

    /**
     * 修改用户信息
     */
    @Transactional
    public void modify(User user) {
        User persist = this.getById(user.getId());

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
        this.updateById(user);

//        userRoleService.relateRoles(user.getId(), user.getRoleIds());
    }


}
