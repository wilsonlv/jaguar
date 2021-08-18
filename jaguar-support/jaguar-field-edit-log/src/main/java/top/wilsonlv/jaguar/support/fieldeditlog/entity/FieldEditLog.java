package top.wilsonlv.jaguar.support.fieldeditlog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 字段编辑日志表
 * </p>
 *
 * @author lvws
 * @since 2019-04-10
 */
@Data
@TableName("jaguar_support_field_edit_log")
public class FieldEditLog implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 类全名
     */
    @TableField("class_name")
    private String className;

    /**
     * 记录ID
     */
    @TableField("record_id")
    private Long recordId;

    /**
     * 字段名称
     */
    @TableField("field_name")
    private String fieldName;

    /**
     * 更新前的值
     */
    @TableField("old_value")
    private String oldValue;

    /**
     * 更新后的值
     */
    @TableField("new_value")
    private String newValue;

    /**
     * 上次更新时间
     */
    @TableField(value = "last_modify_time")
    private LocalDateTime lastModifyTime;

    /**
     * 上次更新人ID
     */
    @TableField(value = "last_modify_user_id")
    private Long lastModifyUserId;

    /**
     * 上次更新人用户名
     */
    @TableField(value = "last_modify_user_name")
    private String lastModifyUserName;

    /**
     * 本次更新时间
     */
    @TableField(value = "modify_time")
    private LocalDateTime modifyTime;

    /**
     * 本次更新人ID
     */
    @TableField(value = "modify_user_id")
    private Long modifyUserId;

    /**
     * 本次更新人用户名
     */
    @TableField(value = "modify_user_name")
    private String modifyUserName;


}