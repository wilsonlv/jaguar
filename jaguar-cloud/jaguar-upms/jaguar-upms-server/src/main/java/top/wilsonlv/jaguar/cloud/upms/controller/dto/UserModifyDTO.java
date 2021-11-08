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
public class UserModifyDTO extends BaseModifyDTO {

    /**
     * 用户账号（唯一）
     */
    @ApiModelProperty(value = "用户账号（唯一）")
    private String userAccount;
    /**
     * 用户手机号（唯一）
     */
    @ApiModelProperty(value = "用户手机号（唯一）")
    private String userPhone;
    /**
     * 用户邮箱（唯一）
     */
    @ApiModelProperty(value = "用户邮箱（唯一）")
    private String userEmail;
    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String userNickName;
    /**
     * 用户是否启用
     */
    @ApiModelProperty(value = "用户是否启用")
    private Boolean userEnable;

}
