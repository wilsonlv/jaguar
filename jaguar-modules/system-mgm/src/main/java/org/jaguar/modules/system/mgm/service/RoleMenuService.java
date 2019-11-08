package org.jaguar.modules.system.mgm.service;

import org.jaguar.modules.system.mgm.model.RoleMenu;
import org.jaguar.modules.system.mgm.mapper.RoleMenuMapper;
import org.jaguar.core.base.BaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统角色菜单表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Service
@CacheConfig(cacheNames = "RoleMenu")
public class RoleMenuService extends BaseService<RoleMenu, RoleMenuMapper>  {

}
