package org.jaguar.modules.system.mgm.service;

import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.BaseService;
import org.jaguar.modules.system.mgm.mapper.UserRoleMapper;
import org.jaguar.modules.system.mgm.model.UserRole;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统用户角色表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-11-15
 */
@Service
public class UserRoleService extends BaseService<UserRole, UserRoleMapper> {

    public List<UserRole> listWithRoleByUserId(Long userId) {
        return this.mapper.listWithRoleByUserId(userId);
    }

    public List<Long> listRoleIdsByUserId(Long userId) {
        List<UserRole> userRoleList = this.list(JaguarLambdaQueryWrapper.<UserRole>newInstance()
                .eq(UserRole::getUserId, userId)
                .select(UserRole::getRoleId));

        List<Long> roleIds = new ArrayList<>();
        for (UserRole userRole : userRoleList) {
            roleIds.add(userRole.getRoleId());
        }
        return roleIds;
    }

}
