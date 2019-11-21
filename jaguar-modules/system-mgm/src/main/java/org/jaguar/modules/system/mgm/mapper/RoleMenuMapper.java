package org.jaguar.modules.system.mgm.mapper;

import org.apache.ibatis.annotations.Param;
import org.jaguar.core.base.BaseMapper;
import org.jaguar.modules.system.enums.MenuType;
import org.jaguar.modules.system.enums.RoleMenuPermission;
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
     * 查询角色菜单，携带菜单信息返回
     */
    List<RoleMenu> listWithMenu(@Param("roleIds") @NotEmpty List<Long> roleIds,
                                @Param("parentId") Long parentId,
                                @Param("menuType") MenuType menuType,
                                @Param("roleMenuPermission") RoleMenuPermission roleMenuPermission);
}