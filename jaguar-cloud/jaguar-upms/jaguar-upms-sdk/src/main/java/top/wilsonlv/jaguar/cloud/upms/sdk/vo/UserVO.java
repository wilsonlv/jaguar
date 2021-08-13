package top.wilsonlv.jaguar.cloud.upms.sdk.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lvws
 * @since 2021/8/10
 */
@Data
public class UserVO implements Serializable {

    private Long id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

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
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码")
    private String userPassword;
    /**
     * 密码上次修改时间
     */
    @ApiModelProperty(value = "密码上次修改时间")
    private LocalDateTime userPasswordLastModifyTime;
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
    /**
     * 用户是否锁定
     */
    @ApiModelProperty(value = "用户是否锁定")
    private Boolean userLocked;

    /**
     * 用户角色
     */
    @ApiModelProperty(value = "用户角色")
    private List<RoleVO> roles;

}
