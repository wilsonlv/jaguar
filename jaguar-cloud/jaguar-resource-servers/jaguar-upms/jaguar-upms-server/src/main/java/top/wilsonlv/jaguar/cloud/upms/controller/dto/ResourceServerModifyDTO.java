package top.wilsonlv.jaguar.cloud.upms.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.web.base.BaseModifyDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * <p>
 * 资源服务器
 * </p>
 *
 * @author lvws
 * @since 2021-12-02
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class ResourceServerModifyDTO extends BaseModifyDTO {

    @NotBlank(message = "服务ID为非空")
    @ApiModelProperty(value = "服务ID", required = true)
    private String serverId;

    @NotBlank(message = "服务名称为非空")
    @ApiModelProperty(value = "服务名称", required = true)
    private String serverName;

    @NotNull(message = "是否展示菜单为非空")
    @ApiModelProperty(value = "是否展示菜单", required = true)
    private Boolean serverMenu;

    @ApiModelProperty(value = "服务网址")
    private String serverUrl;

}