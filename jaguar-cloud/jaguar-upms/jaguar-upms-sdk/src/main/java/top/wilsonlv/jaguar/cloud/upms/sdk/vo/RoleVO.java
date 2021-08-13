package top.wilsonlv.jaguar.cloud.upms.sdk.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2021/8/10
 */
@Data
public class RoleVO implements Serializable {

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    /**
     * 是否内置角色
     */
    @ApiModelProperty(value = "是否内置角色")
    private String roleBuiltIn;
    /**
     * 角色是否启用
     */
    @ApiModelProperty(value = "角色是否启用")
    private Boolean roleEnable;

    public RoleVO() {
    }

    public RoleVO(String roleName, String roleBuiltIn, Boolean roleEnable) {
        this.roleName = roleName;
        this.roleBuiltIn = roleBuiltIn;
        this.roleEnable = roleEnable;
    }
}
