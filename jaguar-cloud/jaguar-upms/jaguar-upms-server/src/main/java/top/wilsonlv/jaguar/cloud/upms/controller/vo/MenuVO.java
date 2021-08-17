package top.wilsonlv.jaguar.cloud.upms.controller.vo;

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

    /**
     * 父ID
     */
    @ApiModelProperty(value = "父ID")
    private Long parentId;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String menuName;
    /**
     * 权限
     */
    @ApiModelProperty(value = "权限")
    private String menuPermission;
    /**
     * 是否为按钮
     */
    @ApiModelProperty(value = "是否为按钮")
    private Boolean menuButton;
    /**
     * 图标
     */
    @ApiModelProperty(value = "图标")
    private String menuIcon;
    /**
     * 展示页面
     */
    @ApiModelProperty(value = "展示页面")
    private String menuPage;
    /**
     * 子菜单
     */
    @ApiModelProperty(value = "子菜单")
    private List<MenuVO> children;

}
