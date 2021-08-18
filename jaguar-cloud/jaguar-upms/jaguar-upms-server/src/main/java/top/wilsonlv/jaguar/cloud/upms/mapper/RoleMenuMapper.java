package top.wilsonlv.jaguar.cloud.upms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.wilsonlv.jaguar.cloud.upms.entity.RoleMenu;
import top.wilsonlv.jaguar.commons.basecrud.BaseMapper;

import java.util.Set;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 根据角色ID集合查询角色权限
     *
     * @param roleIds 角色ID集合
     * @return 角色权限
     */
    Set<String> listPermissionsByRoleIds(@Param("roleIds") Set<Long> roleIds);

}