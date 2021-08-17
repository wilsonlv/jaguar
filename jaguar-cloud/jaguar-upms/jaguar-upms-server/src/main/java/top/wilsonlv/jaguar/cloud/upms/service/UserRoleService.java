package top.wilsonlv.jaguar.cloud.upms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wilsonlv.jaguar.cloud.upms.mapper.UserRoleMapper;
import top.wilsonlv.jaguar.cloud.upms.model.Role;
import top.wilsonlv.jaguar.cloud.upms.model.UserRole;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.RoleVO;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;
import top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统用户角色表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-11-15
 */
@Service
@RequiredArgsConstructor
public class UserRoleService extends BaseService<UserRole, UserRoleMapper> {

    private final RoleService roleService;

    public List<UserVO> listUserByRoleId(@NotNull Long roleId) {
        return this.mapper.listUserByRoleId(roleId);
    }

    public List<RoleVO> listRoleByUserId(@NotNull Long userId) {
        return this.mapper.listRoleByUserId(userId);
    }


    /**
     * 用户关联角色
     *
     * @param userId  用户ID
     * @param roleIds 角色ID集合
     */
    @Transactional
    public void relateRoles(Long userId, Set<Long> roleIds) {
        for (Long roleId : roleIds) {
            Role role = roleService.getById(roleId);

            if (this.exists(JaguarLambdaQueryWrapper.<UserRole>newInstance()
                    .eq(UserRole::getUserId, userId)
                    .eq(UserRole::getRoleId, roleId))) {
                continue;
            }

            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRole.setBuiltIn(false);
            this.insert(userRole);
        }

        this.delete(JaguarLambdaQueryWrapper.<UserRole>newInstance()
                .eq(UserRole::getUserId, userId)
                .notIn(UserRole::getRoleId, roleIds));
    }
}
