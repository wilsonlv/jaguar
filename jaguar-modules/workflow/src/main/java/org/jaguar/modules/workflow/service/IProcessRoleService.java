package org.jaguar.modules.workflow.service;

import java.util.List;

/**
 * @author lvws
 * @since 2019/4/27.
 */
public interface IProcessRoleService {

    /**
     * 通过用户账号获取角色集合
     *
     * @param userAccount 用户账号
     * @return 角色集合
     */
    List<String> queryRoleNamesByUserAccount(String userAccount);

}
