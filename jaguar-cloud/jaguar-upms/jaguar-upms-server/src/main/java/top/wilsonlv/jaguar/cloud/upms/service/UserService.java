package top.wilsonlv.jaguar.cloud.upms.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.klock.annotation.Klock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wilsonlv.jaguar.cloud.upms.constant.LockNameConstant;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.UserCreateDTO;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.UserModifyDTO;
import top.wilsonlv.jaguar.cloud.upms.mapper.UserMapper;
import top.wilsonlv.jaguar.cloud.upms.entity.User;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        UserVO userVO = user.toVo(UserVO.class);

        List<RoleVO> roles = userRoleService.listRoleByUserId(user.getId());
        userVO.setRoles(roles);

        Set<Long> roleIds = roles.stream().map(RoleVO::getId).collect(Collectors.toSet());
        Set<String> permissions = roleMenuService.listPermissionsByRoleIds(roleIds);
        userVO.setPermissions(permissions);

        return userVO;
    }

    /*----------  管理类接口  ----------*/

    @Transactional
    public Page<UserVO> queryWithRole(Page<User> page, LambdaQueryWrapper<User> wrapper) {
        page = this.query(page, wrapper);
        Page<UserVO> voPage = this.toVoPage(page);

        for (User user : page.getRecords()) {
            UserVO userVO = user.toVo(UserVO.class);
            voPage.getRecords().add(userVO);

            List<RoleVO> roles = userRoleService.listRoleByUserId(user.getId());
            userVO.setRoles(roles);
        }
        return voPage;
    }

    @Klock(name = LockNameConstant.USER_CREATE_MODIFY_LOCK)
    @Transactional
    public void create(UserCreateDTO userCreateDTO) {
        if (EncryptionUtil.passwordUnmatched(userCreateDTO.getUserPassword())) {
            throw new CheckedException("密码格式为包含数字，字母大小写的6-20位字符串！");
        }

        User byAccount = this.getByAccount(userCreateDTO.getUserAccount());
        Assert.duplicate(byAccount, "用户账号");

        if (StringUtils.isNotBlank(userCreateDTO.getUserPhone())) {
            User byPhone = this.getByPhone(userCreateDTO.getUserPhone());
            Assert.duplicate(byPhone, "用户手机号");
        }

        if (StringUtils.isNotBlank(userCreateDTO.getUserEmail())) {
            User byEmail = this.getByEmail(userCreateDTO.getUserEmail());
            Assert.duplicate(byEmail, "用户邮箱");
        }

        User user = userCreateDTO.toEntity(User.class);
        user.setUserBuiltIn(false);
        user.setUserLocked(false);
        this.insert(user);

        userRoleService.relateRoles(user.getId(), userCreateDTO.getRoleIds());
    }

    @Klock(name = LockNameConstant.USER_CREATE_MODIFY_LOCK)
    @Transactional
    public void modify(UserModifyDTO userModifyDTO) {
        this.getById(userModifyDTO.getId());

        if (StringUtils.isNotBlank(userModifyDTO.getUserAccount())) {
            User byAccount = this.getByAccount(userModifyDTO.getUserAccount());
            Assert.duplicate(byAccount, userModifyDTO, "用户账号");
        }

        if (StringUtils.isNotBlank(userModifyDTO.getUserPhone())) {
            User byPhone = this.getByPhone(userModifyDTO.getUserPhone());
            Assert.duplicate(byPhone, userModifyDTO, "用户手机号");
        }

        if (StringUtils.isNotBlank(userModifyDTO.getUserEmail())) {
            User byEmail = this.getByEmail(userModifyDTO.getUserEmail());
            Assert.duplicate(byEmail, userModifyDTO, "用户邮箱");
        }

        User user = userModifyDTO.toEntity(User.class);
        this.updateById(user);
    }

    @Klock(name = LockNameConstant.USER_CREATE_MODIFY_LOCK)
    @Transactional
    public void checkAndDelete(Long id) {
        User user = this.getById(id);
        if (user.getUserBuiltIn()) {
            throw new CheckedException("内置用户不可删除");
        }
        this.delete(id);
    }
}
