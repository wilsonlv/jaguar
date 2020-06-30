package org.jaguar.modules.system.mgm.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.core.base.BaseModel;
import org.jaguar.modules.system.mgm.enums.MenuType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Data
@ApiModel
@TableName("jaguar_modules_system_menu")
@EqualsAndHashCode(callSuper = true)
public class Menu extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称", required = true)
    @NotBlank(message = "菜单名称为非空")
    @TableField("menu_name")
    private String menuName;
    /**
     * 上级菜单ID
     */
    @ApiModelProperty(value = "上级菜单ID")
    @TableField("menu_parent_id")
    private Long menuParentId;
    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "上级菜单ID")
    @TableField("menu_icon")
    private String menuIcon;
    /**
     * 菜单URI
     */
    @ApiModelProperty(value = "菜单URI")
    @TableField("menu_page_uri")
    private String menuPageUri;
    /**
     * 菜单排序号
     */
    @ApiModelProperty(value = "菜单排序号")
    @TableField("menu_sort_no")
    private Integer menuSortNo;
    /**
     * 菜单权限名称
     */
    @ApiModelProperty(value = "菜单权限名称")
    @TableField("menu_auth_name")
    private String menuAuthName;
    /**
     * 菜单类型（MENU，FUNCTION）
     */
    @ApiModelProperty(value = "菜单类型", required = true)
    @NotNull(message = "菜单类型为非空")
    @TableField("menu_type")
    private MenuType menuType;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private List<Menu> children;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private RoleMenu roleMenu;
}