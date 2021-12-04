package top.wilsonlv.jaguar.cloud.upms.sdk.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.web.base.BaseVO;

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
public class ResourceServerVO extends BaseVO {

    @ApiModelProperty("服务ID")
    private String serverId;

    @ApiModelProperty("服务名称")
    private String serverName;

    @ApiModelProperty("服务密钥")
    private String serverSecret;

    @ApiModelProperty("是否展示菜单")
    private Boolean serverMenu;

    @ApiModelProperty("服务网址")
    private String serverUrl;

}