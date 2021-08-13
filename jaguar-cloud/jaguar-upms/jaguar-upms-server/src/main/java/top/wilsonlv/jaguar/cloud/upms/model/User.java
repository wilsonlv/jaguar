package top.wilsonlv.jaguar.cloud.upms.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.basecrud.BaseModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Data
@ApiModel
@TableName("jaguar_modules_system_user")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 用户账号（唯一）
     */
    @ApiModelProperty(value = "用户账号（唯一）", required = true)
    @NotBlank(message = "用户账号为非空且唯一")
    @TableField("user_account")
    private String userAccount;
    /**
     * 是否内置用户
     */
    @ApiModelProperty(value = "是否内置用户")
    @TableField("user_built_in")
    private Boolean userBuiltIn;
    /**
     * 用户手机号（唯一）
     */
    @ApiModelProperty(value = "用户手机号（唯一）")
    @TableField("user_phone")
    private String userPhone;
    /**
     * 用户邮箱（唯一）
     */
    @ApiModelProperty(value = "用户邮箱（唯一）")
    @TableField("user_email")
    private String userEmail;
    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码")
    @TableField("user_password")
    private String userPassword;
    /**
     * 密码上次修改时间
     */
    @ApiModelProperty(value = "密码上次修改时间")
    @TableField("user_password_last_modify_time")
    private LocalDateTime userPasswordLastModifyTime;
    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    @TableField("user_nick_name")
    private String userNickName;
    /**
     * 用户是否启用
     */
    @ApiModelProperty(value = "用户是否启用")
    @NotNull(message = "用户是否启用为非空")
    @TableField("user_enable")
    private Boolean userEnable;
    /**
     * 用户是否锁定
     */
    @ApiModelProperty(value = "用户是否锁定")
    @TableField("user_locked")
    private Boolean userLocked;

}