package top.wilsonlv.jaguar.cloud.upms.sdk.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.web.base.BaseVO;

import java.util.List;

/**
 * @author lvws
 * @since 2021/8/17
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class MenuVO extends BaseVO {

    @ApiModelProperty("父ID")
    private Long parentId;

    @ApiModelProperty("是否内置菜单")
    private Boolean menuBuiltIn;

    @ApiModelProperty("名称")
    private String menuName;

    @ApiModelProperty("权限")
    private String menuPermission;

    @ApiModelProperty("排序")
    private String menuOrder;

    @ApiModelProperty("是否为按钮")
    private Boolean menuButton;

    @ApiModelProperty("图标")
    private String menuIcon;

    @ApiModelProperty("展示页面")
    private String menuPage;

    @ApiModelProperty("子菜单")
    private List<MenuVO> children;

}
