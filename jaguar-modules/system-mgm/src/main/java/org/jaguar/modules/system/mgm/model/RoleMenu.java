package org.jaguar.modules.system.mgm.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import org.jaguar.core.base.BaseModel;

/**
 * <p>
 * 系统角色菜单表
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Data
@TableName("t_system_role_menu")
public class RoleMenu extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableField("id")
	private Long id;
    /**
     * 角色ID
     */
	@TableField("role_id")
	private Long roleId;
    /**
     * 菜单ID
     */
	@TableField("menu_id")
	private Long menuId;
    /**
     * 角色菜单权限（READ，VIEW，UPDATE，DEL）
     */
	@TableField("role_menu_permission")
	private String roleMenuPermission;

}