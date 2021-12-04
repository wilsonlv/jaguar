package top.wilsonlv.jaguar.cloud.upms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.wilsonlv.jaguar.cloud.upms.entity.UserRole;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.RoleVO;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
import top.wilsonlv.jaguar.basecrud.BaseMapper;

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
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 查询用户角色
     *
     * @param userId 用户ID
     * @return 用户角色
     */
    List<RoleVO> listRoleByUserId(@Param("userId") @NotNull Long userId);

    /**
     * 查询用户角色
     *
     * @param roleId 角色ID
     * @return 用户角色
     */
    List<UserVO> listUserByRoleId(@Param("roleId") @NotNull Long roleId);

}