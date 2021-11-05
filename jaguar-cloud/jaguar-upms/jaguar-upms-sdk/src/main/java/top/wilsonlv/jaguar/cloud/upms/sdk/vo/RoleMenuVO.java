package top.wilsonlv.jaguar.cloud.upms.sdk.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.web.base.BaseVO;

/**
 * @author lvws
 * @since 2021/11/4
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class RoleMenuVO extends BaseVO {

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("菜单ID")
    private Long menuId;

    @ApiModelProperty("是否内置")
    private Boolean builtIn;

}
