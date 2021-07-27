package top.wilsonlv.jaguar.cloud.upms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.wilsonlv.jaguar.cloud.upms.model.Role;
import top.wilsonlv.jaguar.cloud.upms.model.User;
import top.wilsonlv.jaguar.cloud.upms.model.UserRole;
import top.wilsonlv.jaguar.commons.basecrud.BaseMapper;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author lvws
 * @since 2019-11-15
 */
@Mapper
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

    /**
     * 查询用户菜单
     *
     * @param userId 用户ID
     * @return 权限集合
     */
    Set<String> listMenuFunctionNamesByUserId(@Param("userId") @NotNull Long userId);
}