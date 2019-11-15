package org.jaguar.modules.system.mgm.mapper;

import org.apache.ibatis.annotations.Param;
import org.jaguar.core.base.BaseMapper;
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
     * 查询用户角色，携带角色信息返回
     */
    List<UserRole> listWithRoleByUserId(@Param("userId") @NotNull Long userId);
}