package top.wilsonlv.jaguar.cloud.upms.sdk.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.web.base.BaseVO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/8/10
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class UserVO extends BaseVO {

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long id;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
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
    /**
     * 用户权限
     */
    @ApiModelProperty(value = "用户权限")
    private Set<String> permissions;

}
