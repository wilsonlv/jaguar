package org.jaguar.modules.upms.server.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.commons.basecrud.BaseModel;
import org.jaguar.modules.upms.sdk.dto.MenuFunction;
import org.jaguar.modules.upms.sdk.enums.DataScope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

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
public class User extends BaseModel implements UserDetails {

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
     * 个人数据权限
     */
    @ApiModelProperty(value = "个人数据权限", required = true)
    @NotNull(message = "个人数据权限为非空")
    @TableField("user_data_scope")
    private DataScope userDataScope;
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

    @ApiModelProperty(value = "用户角色ID集合")
    @TableField(exist = false)
    private List<Long> roleIds = new ArrayList<>();

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private List<Role> roles = new ArrayList<>();


    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private Set<GrantedAuthority> authorities = new HashSet<>();

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private List<MenuFunction> menuFunctions = new ArrayList<>();

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.userAccount;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.userLocked == null || !this.userLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.userEnable != null ? this.userEnable : true;
    }
}