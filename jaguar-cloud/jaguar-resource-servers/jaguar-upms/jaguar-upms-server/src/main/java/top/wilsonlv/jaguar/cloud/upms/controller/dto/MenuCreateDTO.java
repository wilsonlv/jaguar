package top.wilsonlv.jaguar.cloud.upms.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.web.base.BaseDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lvws
 * @since 2021/8/16
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class MenuCreateDTO extends BaseDTO {

    @NotNull(message = "资源服务ID为非空")
    @ApiModelProperty(value = "资源服务ID", required = true)
    private Long resourceServerId;

    @NotNull(message = "父ID为非空")
    @ApiModelProperty(value = "父ID", required = true)
    private Long parentId;

    @NotBlank(message = "名称为非空")
    @ApiModelProperty(value = "名称（唯一）", required = true)
    private String menuName;

    @NotBlank(message = "权限为非空")
    @ApiModelProperty(value = "权限（唯一）", required = true)
    private String menuPermission;

    @ApiModelProperty("排序")
    private Integer menuOrder;

    @NotNull(message = "是否为按钮为非空")
    @ApiModelProperty(value = "是否为按钮", required = true)
    private Boolean menuButton;

    @ApiModelProperty("menu_icon")
    private String menuIcon;

    @ApiModelProperty("menu_page")
    private String menuPage;

}
