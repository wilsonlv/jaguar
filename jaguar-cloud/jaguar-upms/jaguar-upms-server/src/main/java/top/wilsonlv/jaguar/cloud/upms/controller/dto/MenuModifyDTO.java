package top.wilsonlv.jaguar.cloud.upms.controller.dto;

import com.baomidou.mybatisplus.annotation.TableField;
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

    @ApiModelProperty("父ID")
    private Long parentId;

    @ApiModelProperty("menu_name")
    private String menuName;

    @ApiModelProperty("menu_permission")
    private String menuPermission;

    @ApiModelProperty("排序")
    private String menuOrder;

    @ApiModelProperty("menu_button")
    private Boolean menuButton;

    @ApiModelProperty("menu_icon")
    private String menuIcon;

    @ApiModelProperty("menu_page")
    private String menuPage;

}
