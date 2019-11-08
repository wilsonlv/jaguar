package org.jaguar.modules.system.mgm.service;

import org.jaguar.modules.system.mgm.model.Menu;
import org.jaguar.modules.system.mgm.mapper.MenuMapper;
import org.jaguar.core.base.BaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统菜单表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Service
@CacheConfig(cacheNames = "Menu")
public class MenuService extends BaseService<Menu, MenuMapper>  {

}
