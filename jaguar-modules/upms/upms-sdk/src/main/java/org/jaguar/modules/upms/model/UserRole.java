package org.jaguar.modules.upms.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.commons.basecrud.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 系统用户角色表
 * </p>
 *
 * @author lvws
 * @since 2019-11-15
 */
@Data
@TableName("jaguar_modules_system_user_role")
@EqualsAndHashCode(callSuper = true)
public class UserRole extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID为非空")
    @TableField("user_id")
    private Long userId;
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", required = true)
    @NotNull(message = "角色ID为非空")
    @TableField("role_id")
    private Long roleId;

}