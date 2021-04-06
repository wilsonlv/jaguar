package org.jaguar.modules.system.mgm.mapper;

import org.apache.ibatis.annotations.Param;
import org.jaguar.commons.basecrud.BaseMapper;
import org.jaguar.modules.system.mgm.model.Role;
import org.jaguar.modules.system.mgm.model.User;
import org.jaguar.modules.system.mgm.model.UserRole;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author lvws
 * @since 2019-11-15
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 查询用户角色
     *
     * @param userId 用户ID
     * @return 用户角色
     */
    List<Role> listRoleByUserId(@Param("userId") @NotNull Long userId);

    /**
     * 查询用户角色
     *
     * @param roleId 角色ID
     * @return 用户角色
     */
    List<User> listUserByRoleId(@Param("roleId") @NotNull Long roleId);
}