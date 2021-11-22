package top.wilsonlv.jaguar.cloud.upms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.basecrud.BaseModel;
import top.wilsonlv.jaguar.support.datamodifylog.entity.DataModifyLoggable;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author lvws
 * @since 2021-08-16
 */
@Data
@TableName("jaguar_cloud_upms_user")
@EqualsAndHashCode(callSuper = true)
public class User extends DataModifyLoggable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户账号（唯一）
     */
    @TableField("user_account")
    private String userAccount;
    /**
     * 是否内置用户
     */
    @TableField("user_built_in")
    private Boolean userBuiltIn;
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
     * 密码上次修改时间
     */
    @TableField("user_password_last_modify_time")
    private LocalDateTime userPasswordLastModifyTime;
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
     * 用户是否启用
     */
    @TableField("user_enable")
    private Boolean userEnable;
    /**
     * 用户是否锁定
     */
    @TableField("user_locked")
    private Boolean userLocked;

}