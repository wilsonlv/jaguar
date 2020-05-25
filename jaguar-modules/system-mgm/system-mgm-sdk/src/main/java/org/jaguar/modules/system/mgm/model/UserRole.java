package org.jaguar.modules.system.mgm.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.core.base.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 系统用户角色表
 * </p>
 *
 * @author lvws
 * @since 2019-11-15
 */
@Data
@TableName("jaguar_modules_system_user_role")
@EqualsAndHashCode(callSuper = true)
public class UserRole extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID为非空")
    @TableField("user_id")
    private Long userId;
    /**
     * 角色ID
     */
    @NotNull(message = "角色ID为非空")
    @TableField("role_id")
    private Long roleId;

    @TableField(exist = false)
    private Role role;

    @TableField(exist = false)
    private User user;

}