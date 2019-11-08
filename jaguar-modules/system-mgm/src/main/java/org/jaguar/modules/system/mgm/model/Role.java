package org.jaguar.modules.system.mgm.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import org.jaguar.core.base.BaseModel;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Data
@TableName("t_system_role")
public class Role extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableField("id")
	private Long id;
    /**
     * 角色名称
     */
	@TableField("role_name")
	private String roleName;
    /**
     * 角色数据权限（OWNER、DEPT、ALL）
     */
	@TableField("role_data_scope")
	private String roleDataScope;
    /**
     * 角色是否锁定
     */
	@TableField("role_locked")
	private Boolean roleLocked;

}