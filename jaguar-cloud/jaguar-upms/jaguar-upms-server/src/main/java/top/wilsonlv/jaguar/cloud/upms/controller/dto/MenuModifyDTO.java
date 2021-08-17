package top.wilsonlv.jaguar.cloud.upms.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.web.base.BaseModifyDTO;

/**
 * @author lvws
 * @since 2021/8/16
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class MenuModifyDTO extends BaseModifyDTO {

    /**
     * 父ID
     */
    @ApiModelProperty(value = "父ID")
    private Long parentId;
    /**
     * 名称
     */
    @ApiModelProperty(value = "menu_name")
    private String menuName;
    /**
     * 权限
     */
    @ApiModelProperty(value = "menu_permission")
    private String menuPermission;
    /**
     * 是否为按钮
     */
    @ApiModelProperty(value = "menu_button")
    private Boolean menuButton;
    /**
     * 图标
     */
    @ApiModelProperty("menu_icon")
    private String menuIcon;
    /**
     * 展示页面
     */
    @ApiModelProperty("menu_page")
    private String menuPage;

}
