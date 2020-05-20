package org.jaguar.modules.workflow.service;

/**
 * @author lvws
 * @since 2019/6/20.
 */
public interface IProcessOrgService {

    /**
     * 通用用户账号获取组织机构名称
     * @param userAccount 用户账号
     * @return 组织机构名称
     */
    String getOrgNameByUserAccount(String userAccount);

}
