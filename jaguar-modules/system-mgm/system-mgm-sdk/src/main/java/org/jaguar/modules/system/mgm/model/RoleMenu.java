package org.jaguar.modules.system.mgm.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.core.base.BaseModel;
import org.jaguar.modules.system.mgm.enums.RoleMenuPermission;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 系统角色菜单表
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Data
@ApiModel
@TableName("jaguar_modules_system_role_menu")
@EqualsAndHashCode(callSuper = true)
public class RoleMenu extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", required = true)
    @NotNull(message = "角色ID为非空")
    @TableField("role_id")
    private Long roleId;
    /**
     * 菜单ID
     */
    @ApiModelProperty(value = "菜单ID", required = true)
    @NotNull(message = "菜单ID为非空")
    @TableField("menu_id")
    private Long menuId;
    /**
     * 角色菜单权限（READ，VIEW，UPDATE，DEL）
     */
    @ApiModelProperty(value = "角色菜单权限")
    @TableField("role_menu_permission")
    private RoleMenuPermission roleMenuPermission;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private Menu menu;

}