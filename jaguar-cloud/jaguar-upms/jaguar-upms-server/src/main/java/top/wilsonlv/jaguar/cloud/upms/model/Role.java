package top.wilsonlv.jaguar.cloud.upms.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.basecrud.BaseModel;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author lvws
 * @since 2021-08-16
 */
@Data
@TableName("jaguar_cloud_upms_role")
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;
    /**
     * 是否内置角色
     */
    @TableField("role_built_in")
    private String roleBuiltIn;
    /**
     * 角色是否启用
     */
    @TableField("role_enable")
    private Boolean roleEnable;

}