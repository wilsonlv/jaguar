package org.jaguar.modules.system.mgm.service;

import org.jaguar.commons.enums.ClientType;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.modules.system.mgm.mapper.LoginMapper;
import org.jaguar.modules.system.mgm.model.Login;
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
public class LoginService extends BaseService<Login, LoginMapper> {

    /**
     * 根据用户ID和客户端类型查询登录日志
     *
     * @param userId     用户ID
     * @param clientType 客户端类型
     * @return 登录日志
     */
    public Login findLatestByUserIdAndClientType(Long userId, ClientType clientType) {
        return this.unique(JaguarLambdaQueryWrapper.<Login>newInstance()
                .eq(Login::getUserId, userId)
                .eq(Login::getClientType, clientType)
                .orderByDesc(Login::getLoginTime)
                .last("limit 1"));
    }

    /**
     * 根据用户ID和sessionId查询登录日志
     *
     * @param userId    用户ID
     * @param sessionId sessionId
     * @return 登录日志
     */
    public Login findLatestByUserIdAndSessionId(Long userId, String sessionId) {
        return this.unique(JaguarLambdaQueryWrapper.<Login>newInstance()
                .eq(Login::getUserId, userId)
                .eq(Login::getSessionId, sessionId)
                .orderByDesc(Login::getLoginTime)
                .last("limit 1"));
    }

}
