package org.jaguar.modules.workflow.model.po;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2019/4/27.
 */
public interface IProcessUser extends Serializable {

    /**
     * 账号
     *
     * @return 账号
     */
    String getAccount();

    /**
     * 手机号
     *
     * @return 手机号
     */
    String getPhone();

    /**
     * 邮箱
     *
     * @return 邮箱
     */
    String getEmail();

}
