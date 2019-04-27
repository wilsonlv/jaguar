package com.jaguar.process.service;

import com.jaguar.process.model.po.ProcessRole;

/**
 * Created by lvws on 2019/4/27.
 */
public interface IProcessRoleService {

    ProcessRole getByUserAccount(String userAccount);

}
