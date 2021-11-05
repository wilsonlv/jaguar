package top.wilsonlv.jaguar.cloud.upms.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/8/16
 */
@Data
@ApiModel
public class RoleCreateDTO implements Serializable {

    @NotBlank(message = "角色名称为非空")
    @ApiModelProperty(value = "角色名称", required = true)
    private String roleName;

    @NotNull(message = "角色是否启用为非空")
    @ApiModelProperty(value = "角色是否启用", required = true)
    private Boolean roleEnable;

    @NotNull(message = "角色菜单ID集合为非空")
    @ApiModelProperty(value = "角色菜单ID集合", required = true)
    private Set<Long> menuIds;

}
