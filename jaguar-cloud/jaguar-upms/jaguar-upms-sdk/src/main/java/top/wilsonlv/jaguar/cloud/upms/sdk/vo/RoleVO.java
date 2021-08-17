package top.wilsonlv.jaguar.cloud.upms.sdk.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.web.base.BaseVO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/8/10
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class RoleVO extends BaseVO {

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long id;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    /**
     * 是否内置角色
     */
    @ApiModelProperty(value = "是否内置角色")
    private Boolean roleBuiltIn;
    /**
     * 角色是否启用
     */
    @ApiModelProperty(value = "角色是否启用")
    private Boolean roleEnable;
    /**
     * 角色用户
     */
    @ApiModelProperty(value = "角色用户")
    private List<UserVO> users;
    /**
     * 菜单ID集合
     */
    @ApiModelProperty(value = "菜单ID集合")
    private Set<Long> menuIds;


    public RoleVO() {
    }

    public RoleVO(String roleName, Boolean roleBuiltIn, Boolean roleEnable) {
        this.roleName = roleName;
        this.roleBuiltIn = roleBuiltIn;
        this.roleEnable = roleEnable;
    }


}
