package org.jaguar.modules.workflow.service;

import org.jaguar.modules.workflow.model.po.IProcessUser;

/**
 * Created by lvws on 2019/4/27.
 */
public interface IProcessUserService {

    IProcessUser getByAccount(String account);

}
