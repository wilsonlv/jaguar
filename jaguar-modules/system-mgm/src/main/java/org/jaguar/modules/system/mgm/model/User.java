package org.jaguar.modules.system.mgm.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import org.jaguar.core.base.BaseModel;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Data
@TableName("t_system_user")
public class User extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableField("id")
	private Long id;
    /**
     * 用户账号（唯一）
     */
	@TableField("user_account")
	private String userAccount;
    /**
     * 用户手机号（唯一）
     */
	@TableField("user_phone")
	private String userPhone;
    /**
     * 用户邮箱（唯一）
     */
	@TableField("user_email")
	private String userEmail;
    /**
     * 用户密码
     */
	@TableField("user_password")
	private String userPassword;
    /**
     * 用户昵称
     */
	@TableField("user_nick_name")
	private String userNickName;
    /**
     * 用户部门ID
     */
	@TableField("user_dept_id")
	private Long userDeptId;
    /**
     * 用户是否锁定
     */
	@TableField("user_locked")
	private Boolean userLocked;

}