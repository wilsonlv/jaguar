package org.jaguar.modules.system.mgm.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.core.base.BaseModel;

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
    @TableField("user_id")
    private Long userId;
    /**
     * 角色ID
     */
    @TableField("role_id")
    private Long roleId;

    @TableField(exist = false)
    private Role role;

}