package org.jaguar.cloud.upms.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.commons.basecrud.BaseModel;
import org.jaguar.cloud.upms.sdk.dto.MenuFunction;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Data
@ApiModel
@TableName("jaguar_modules_system_role")
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", required = true)
    @NotBlank(message = "角色名称为非空")
    @TableField("role_name")
    private String roleName;
    /**
     * 角色是否启用
     */
    @ApiModelProperty(value = "角色是否启用", required = true)
    @NotNull(message = "角色是否启用为非空")
    @TableField("role_enable")
    private Boolean roleEnable;

    @ApiModelProperty(value = "菜单和功能名称集合")
    @TableField(exist = false)
    private Set<String> menuFunctionNames = new HashSet<>();

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private List<User> users = new ArrayList<>();

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private List<MenuFunction> menuFunctions = new ArrayList<>();

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private List<RoleMenu> roleMenus = new ArrayList<>();

}