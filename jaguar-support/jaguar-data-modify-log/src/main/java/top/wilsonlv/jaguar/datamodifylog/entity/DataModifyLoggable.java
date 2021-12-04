package top.wilsonlv.jaguar.datamodifylog.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.basecrud.BaseModel;

/**
 * @author lvws
 * @since 2021/3/30.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataModifyLoggable extends BaseModel {

    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    @TableField(value = "update_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateUserId;

    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

}
