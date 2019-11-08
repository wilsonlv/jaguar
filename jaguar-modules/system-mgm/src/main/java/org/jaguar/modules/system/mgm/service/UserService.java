package org.jaguar.modules.system.mgm.service;


import org.jaguar.core.base.BaseService;
import org.jaguar.modules.system.mgm.mapper.UserMapper;
import org.jaguar.modules.system.mgm.model.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Service
@CacheConfig(cacheNames = "User")
public class UserService extends BaseService<User, UserMapper> {

}
