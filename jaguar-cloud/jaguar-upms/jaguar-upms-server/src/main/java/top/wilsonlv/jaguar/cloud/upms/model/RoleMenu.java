package top.wilsonlv.jaguar.cloud.upms.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.basecrud.BaseModel;

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
     * 菜单或功能名称
     */
    @ApiModelProperty(value = "菜单或功能名称", required = true)
    @NotNull(message = "菜单或功能名称为非空")
    @TableField("menu_function_name")
    private String menuFuncionName;


}