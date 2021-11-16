package top.wilsonlv.jaguar.cloud.upms.controller.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.web.base.BaseModifyDTO;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/8/16
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class UserModifyDTO extends BaseModifyDTO {

    @ApiModelProperty(value = "用户账号（唯一）")
    private String userAccount;

    @ApiModelProperty(value = "用户手机号（唯一）")
    private String userPhone;

    @ApiModelProperty(value = "用户邮箱（唯一）")
    private String userEmail;

    @ApiModelProperty(value = "用户昵称")
    private String userNickName;

    @ApiModelProperty(value = "用户部门ID")
    private Long userDeptId;

    @ApiModelProperty(value = "用户是否启用")
    private Boolean userEnable;

    @NotEmpty(message = "用户角色ID集合为非空")
    @ApiModelProperty(value = "用户角色ID集合", required = true)
    private Set<Long> roleIds;

}
