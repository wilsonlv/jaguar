package org.jaguar.modules.workflow.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jaguar.core.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 表单数据附件表
 * </p>
 *
 * @author lvws
 * @since 2019-04-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_workflow_form_data_attach")
public class FormDataAttach extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 表单数据表ID
     */
    @TableField("form_data_id")
    private Long formDataId;
    /**
     * 文档ID
     */
    @TableField("document_id")
    private Long documentId;

}