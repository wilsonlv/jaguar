package top.wilsonlv.jaguar.cloud.upms.sdk.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.web.base.BaseVO;

import java.util.List;

/**
 * @author lvws
 * @since 2021/8/10
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class RoleVO extends BaseVO {

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "是否内置角色")
    private Boolean roleBuiltIn;

    @ApiModelProperty(value = "角色是否启用")
    private Boolean roleEnable;

    @ApiModelProperty(value = "角色用户")
    private List<UserVO> users;

    @ApiModelProperty(value = "角色菜单集合")
    private List<RoleMenuVO> roleMenus;

}
