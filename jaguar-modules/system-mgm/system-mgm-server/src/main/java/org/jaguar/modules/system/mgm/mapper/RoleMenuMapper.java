package org.jaguar.modules.system.mgm.mapper;

import org.apache.ibatis.annotations.Param;
import org.jaguar.core.base.BaseMapper;
import org.jaguar.modules.system.mgm.model.Menu;
import org.jaguar.modules.system.mgm.model.RoleMenu;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 根据角色ID集合和父菜单ID查询"可查看"权限的菜单
     *
     * @param roleIds  角色ID集合
     * @param parentId 父菜单ID
     * @return 菜单集合
     */
    List<Menu> listViewMenusByRoleIdsAndParentId(@Param("roleIds") @NotEmpty List<Long> roleIds, @Param("parentId") Long parentId);

    /**
     * 根据角色ID集合查询"最高的"菜单权限，写单菜单信息返回
     *
     * @param roleIds
     * @return
     */
    List<RoleMenu> listMaxPermissionsWithMenuByRoleIds(@Param("roleIds") @NotEmpty List<Long> roleIds);
}