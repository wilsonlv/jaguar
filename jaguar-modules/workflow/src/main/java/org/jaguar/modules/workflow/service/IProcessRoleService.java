package org.jaguar.modules.workflow.service;

import java.util.List;

/**
 * Created by lvws on 2019/4/27.
 */
public interface IProcessRoleService {

    List<String> queryRoleNamesByUserAccount(String userAccount);

}
