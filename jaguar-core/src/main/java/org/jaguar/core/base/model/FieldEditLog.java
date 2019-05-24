package org.jaguar.core.base.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jaguar.core.base.BaseModel;

/**
 * <p>
 * 字段编辑日志表
 * </p>
 *
 * @author lvws
 * @since 2019-04-10
 */
@TableName("t_field_edit_log")
public class FieldEditLog extends BaseModel {

    private static final long serialVersionUID = 1L;

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


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

}