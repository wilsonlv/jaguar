package org.jaguar.modules.system.mgm.service;

import org.jaguar.modules.system.mgm.model.Role;
import org.jaguar.modules.system.mgm.mapper.RoleMapper;
import org.jaguar.core.base.BaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Service
@CacheConfig(cacheNames = "Role")
public class RoleService extends BaseService<Role, RoleMapper>  {

}
