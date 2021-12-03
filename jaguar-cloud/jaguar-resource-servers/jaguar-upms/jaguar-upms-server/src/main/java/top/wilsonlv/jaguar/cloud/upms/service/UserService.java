package top.wilsonlv.jaguar.cloud.upms.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.klock.annotation.Klock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wilsonlv.jaguar.cloud.upms.constant.LockNameConstant;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.UserCreateDTO;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.UserModifyDTO;
import top.wilsonlv.jaguar.cloud.upms.entity.User;
import top.wilsonlv.jaguar.cloud.upms.entity.UserRole;
import top.wilsonlv.jaguar.cloud.upms.mapper.UserMapper;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.RoleVO;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
import top.wilsonlv.jaguar.commons.basecrud.Assert;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;
import top.wilsonlv.jaguar.commons.basecrud.util.LongUtil;
import top.wilsonlv.jaguar.commons.data.encryption.util.EncryptionUtil;
import top.wilsonlv.jaguar.commons.rediscache.AbstractRedisCacheService;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
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
public class UserService extends AbstractRedisCacheService<User, UserMapper> {

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private RoleMenuService roleMenuService;

    @Resource
    private DeptService deptService;

    @Resource
    private PasswordEncoder passwordEncoder;


    /*----------  通用接口  ----------*/

    public User getByAccount(@NotBlank String account) {
        return this.unique(Wrappers.lambdaQuery(User.class)
                .eq(User::getUserAccount, account));
    }

    public User getByPhone(@NotBlank String phone) {
        return this.unique(Wrappers.lambdaQuery(User.class)
                .eq(User::getUserPhone, phone));
    }

    public User getByEmail(@NotBlank String email) {
        return this.unique(Wrappers.lambdaQuery(User.class)
                .eq(User::getUserEmail, email));
    }

    /**
     * 获取用户详情
     *
     * @param currentUser 用户ID
     * @return 用户详情
     */
    @Transactional
    public UserVO getDetail(Long currentUser) {
        User user = this.getCache(currentUser);
        UserVO userVO = user.toVo(UserVO.class);

        List<RoleVO> roles = userRoleService.listRoleByUserId(user.getId());
        userVO.setRoles(roles);

        Set<Long> roleIds = roles.stream().map(RoleVO::getId).collect(Collectors.toSet());
        Set<String> permissions = roleMenuService.listPermissionsByRoleIds(roleIds);
        userVO.setPermissions(permissions);

        if (LongUtil.notNull(user.getUserDeptId())) {
            userVO.setDept(deptService.getDetail(user.getUserDeptId()));
        }
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

            if (LongUtil.notNull(user.getUserDeptId())) {
                userVO.setDept(deptService.getDetail(user.getUserDeptId()));
            }
        }
        return voPage;
    }

    @Klock(name = LockNameConstant.USER_CREATE_MODIFY_LOCK)
    @Transactional
    public void create(UserCreateDTO userCreateDTO) {
        if (EncryptionUtil.passwordUnmatched(userCreateDTO.getUserPassword())) {
            throw new CheckedException("密码格式为包含数字，字母大小写的6-20位字符串！");
        }
        userCreateDTO.setUserPassword(passwordEncoder.encode(userCreateDTO.getUserPassword()));

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
        user.setUserPasswordLastModifyTime(LocalDateTime.now());
        this.insert(user);

        userRoleService.relateRoles(user.getId(), userCreateDTO.getRoleIds());
    }

    @Klock(name = LockNameConstant.USER_CREATE_MODIFY_LOCK)
    @Transactional
    public void modify(UserModifyDTO userModifyDTO) {
        User byAccount = this.getByAccount(userModifyDTO.getUserAccount());
        Assert.duplicate(byAccount, userModifyDTO, "用户账号");

        if ("".equals(userModifyDTO.getUserPassword())) {
            userModifyDTO.setUserPassword(null);
        }

        LocalDateTime userPasswordLastModifyTime = null;
        if (StringUtils.isNotBlank(userModifyDTO.getUserPassword())) {
            if (EncryptionUtil.passwordUnmatched(userModifyDTO.getUserPassword())) {
                throw new CheckedException("密码格式为包含数字，字母大小写的6-20位字符串！");
            }
            userModifyDTO.setUserPassword(passwordEncoder.encode(userModifyDTO.getUserPassword()));
            userPasswordLastModifyTime = LocalDateTime.now();
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
        user.setUserPasswordLastModifyTime(userPasswordLastModifyTime);
        this.updateById(user);

        userRoleService.relateRoles(user.getId(), userModifyDTO.getRoleIds());
    }

    @Klock(name = LockNameConstant.USER_CREATE_MODIFY_LOCK)
    @Transactional
    public void checkAndDelete(Long id) {
        User user = this.getById(id);
        if (user.getUserBuiltIn()) {
            throw new CheckedException("内置用户不可修改或删除");
        }
        this.delete(id);

        userRoleService.delete(Wrappers.lambdaQuery(UserRole.class)
                .eq(UserRole::getUserId, id));
    }
}
