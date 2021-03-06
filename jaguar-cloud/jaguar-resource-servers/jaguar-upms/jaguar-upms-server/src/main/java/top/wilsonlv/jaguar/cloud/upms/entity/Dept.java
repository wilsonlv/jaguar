package top.wilsonlv.jaguar.cloud.upms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.datamodifylog.entity.DataModifyLoggable;

/**
 * <p>
 * 部门
 * </p>
 *
 * @author lvws
 * @since 2021-11-15
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@TableName("jaguar_cloud_upms_dept")
public class Dept extends DataModifyLoggable {

    /**
     * 父ID
     */
    @ApiModelProperty(value = "父ID")
    @TableField("parent_id")
    private Long parentId;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    @TableField("dept_name")
    private String deptName;

}