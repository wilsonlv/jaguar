package org.jaguar.modules.system.mgm.service;

import org.jaguar.core.base.BaseService;
import org.jaguar.modules.system.mgm.mapper.LoginMapper;
import org.jaguar.modules.system.mgm.model.Login;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统登陆日志表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-11-15
 */
@Service
@CacheConfig(cacheNames = "Login")
public class LoginService extends BaseService<Login, LoginMapper> {

}
