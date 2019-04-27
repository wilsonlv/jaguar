package com.jaguar.process.service;

import com.jaguar.process.model.po.IProcessUser;

/**
 * Created by lvws on 2019/4/27.
 */
public interface IProcessUserService {

    IProcessUser getByAccount(String account);

}
