package com.jaguar.process.model.po;

import com.jaguar.core.base.BaseModel;

/**
 * <p>
 * 表单数据附件表
 * </p>
 *
 * @author lvws
 * @since 2019-04-27
 */
public class FormDataAttach extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 表单数据表ID
     */
	private Long formDataId;
    /**
     * 值
     */
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