package org.jaguar.modules.workflow.model.po;

import java.io.Serializable;

/**
 * Created by lvws on 2019/4/27.
 */
public interface IProcessUser extends Serializable {

    String getAccount();

    String getPhone();

    String getEmail();

    String getUserName();

}
