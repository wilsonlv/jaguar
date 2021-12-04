package top.wilsonlv.jaguar.cloud.upms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.basecrud.BaseModel;

/**
 * <p>
 * 用户角色表
 * </p>
 *
 * @author lvws
 * @since 2021-08-16
 */
@Data
@TableName("jaguar_cloud_upms_user_role")
@EqualsAndHashCode(callSuper = true)
public class UserRole extends BaseModel {

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 角色ID
     */
    @TableField("role_id")
    private Long roleId;
    /**
     * 是否内置
     */
    @TableField("built_in")
    private Boolean builtIn;

}