package top.wilsonlv.jaguar.cloud.upms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import top.wilsonlv.jaguar.commons.basecrud.BaseModel;
import top.wilsonlv.jaguar.support.datamodifylog.entity.DataModifyLoggable;

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

    private static final long serialVersionUID = 1L;

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