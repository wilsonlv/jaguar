package org.jaguar.modules.system.mgm.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.core.base.BaseModel;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

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
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField("user_password")
    private String userPassword;
    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    @TableField("user_nick_name")
    private String userNickName;
    /**
     * 用户部门ID
     */
    @ApiModelProperty(value = "用户部门ID")
    @TableField(value = "user_dept_id")
    private Long userDeptId;
    /**
     * 用户是否锁定
     */
    @ApiModelProperty(value = "用户是否锁定")
    @TableField("user_locked")
    private Boolean userLocked;
    /**
     * 用户初始密码
     */
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String initialPassword;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private List<UserRole> userRoleList = new ArrayList<>();
}