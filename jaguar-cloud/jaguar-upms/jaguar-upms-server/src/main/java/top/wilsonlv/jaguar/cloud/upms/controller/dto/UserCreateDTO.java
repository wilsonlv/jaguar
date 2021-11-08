package top.wilsonlv.jaguar.cloud.upms.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.web.base.BaseDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/8/16
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class UserCreateDTO extends BaseDTO {

    /**
     * 用户账号（唯一）
     */
    @NotBlank(message = "用户账号为非空")
    @ApiModelProperty(value = "用户账号（唯一）", required = true)
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
     * 用户密码
     */
    @NotBlank(message = "用户密码为非空")
    @ApiModelProperty(value = "用户密码", required = true)
    private String userPassword;
    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String userNickName;
    /**
     * 用户是否启用
     */
    @NotNull(message = "用户是否启用为非空")
    @ApiModelProperty(value = "用户是否启用", required = true)
    private Boolean userEnable;
    /**
     * 用户角色ID集合
     */
    @NotEmpty(message = "用户角色ID集合为非空")
    @ApiModelProperty(value = "用户角色ID集合", required = true)
    private Set<Long> roleIds;

}
