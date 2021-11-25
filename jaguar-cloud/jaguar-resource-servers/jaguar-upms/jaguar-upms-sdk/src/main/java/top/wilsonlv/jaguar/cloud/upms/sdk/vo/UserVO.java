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

    @ApiModelProperty("用户账号（唯一）")
    private String userAccount;

    @ApiModelProperty("是否内置用户")
    private Boolean userBuiltIn;

    @ApiModelProperty("用户手机号（唯一）")
    private String userPhone;

    @ApiModelProperty("用户邮箱（唯一）")
    private String userEmail;

    @ApiModelProperty("用户密码")
    private String userPassword;

    @ApiModelProperty("密码上次修改时间")
    private LocalDateTime userPasswordLastModifyTime;

    @ApiModelProperty("用户昵称")
    private String userNickName;

    @ApiModelProperty("用户部门ID")
    private Long userDeptId;

    @ApiModelProperty("用户是否启用")
    private Boolean userEnable;

    @ApiModelProperty("用户是否锁定")
    private Boolean userLocked;

    @ApiModelProperty("用户角色")
    private List<RoleVO> roles;

    @ApiModelProperty("用户权限")
    private Set<String> permissions;

    @ApiModelProperty("用户部门")
    private DeptVO dept;

}
