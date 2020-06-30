package org.jaguar.modules.workflow.service;

import org.jaguar.modules.workflow.model.po.IProcessUser;

/**
 * @author lvws
 * @since 2019/4/27.
 */
public interface IProcessUserService {

    /**
     * 根据用户账号查询用户实体
     *
     * @param account 用户账号
     * @return 用户实体
     */
    IProcessUser getByAccount(String account);

}
