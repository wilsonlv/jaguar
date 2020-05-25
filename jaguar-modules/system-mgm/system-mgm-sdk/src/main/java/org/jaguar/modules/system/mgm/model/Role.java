package org.jaguar.modules.system.mgm.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.core.base.BaseModel;
import org.jaguar.modules.system.mgm.enums.RoleDataScope;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Data
@TableName("jaguar_modules_system_role")
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称为非空")
    @TableField("role_name")
    private String roleName;
    /**
     * 角色数据权限（OWNER、DEPT、ALL）
     */
    @NotNull(message = "角色数据权限为非空")
    @TableField("role_data_scope")
    private RoleDataScope roleDataScope;
    /**
     * 角色是否锁定
     */
    @NotNull(message = "角色是否锁定为非空")
    @TableField("role_locked")
    private Boolean roleLocked;

    @TableField(exist = false)
    private List<UserRole> userRoleList = new ArrayList<>();

}