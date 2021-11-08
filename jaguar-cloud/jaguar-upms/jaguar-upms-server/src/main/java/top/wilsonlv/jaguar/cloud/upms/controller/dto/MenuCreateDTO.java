package top.wilsonlv.jaguar.cloud.upms.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.web.base.BaseDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lvws
 * @since 2021/8/16
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class MenuCreateDTO extends BaseDTO {

    /**
     * 父ID
     */
    @ApiModelProperty(value = "父ID")
    private Long parentId;
    /**
     * 名称（唯一）
     */
    @NotBlank(message = "名称为非空")
    @ApiModelProperty(value = "名称（唯一）", required = true)
    private String menuName;
    /**
     * 权限（唯一）
     */
    @NotBlank(message = "权限为非空")
    @ApiModelProperty(value = "权限（唯一）", required = true)
    private String menuPermission;
    /**
     * 是否为按钮
     */
    @NotNull(message = "是否为按钮为非空")
    @ApiModelProperty(value = "是否为按钮", required = true)
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
