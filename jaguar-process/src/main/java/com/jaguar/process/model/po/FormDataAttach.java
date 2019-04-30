package com.jaguar.process.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jaguar.core.base.BaseModel;

/**
 * <p>
 * 表单数据附件表
 * </p>
 *
 * @author lvws
 * @since 2019-04-27
 */
@TableName("form_data_attach")
public class FormDataAttach extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 表单数据表ID
     */
    @TableField("form_data_id")
    private Long formDataId;
    /**
     * 值
     */
    @TableField("value_")
    private String value;


    public Long getFormDataId() {
        return formDataId;
    }

    public void setFormDataId(Long formDataId) {
        this.formDataId = formDataId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}