package org.jaguar.modules.workflow.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.core.base.BaseModel;
import org.jaguar.modules.workflow.enums.FormDataPersistenceType;

/**
 * <p>
 * 表单数据表
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("jaguar_modules_workflow_form_data")
public class FormData extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 流程信息ID
     */
    @TableField("process_info_id")
    private Long processInfoId;
    /**
     * 表单模版ID
     */
    @TableField("form_template_id")
    private Long formTemplateId;
    /**
     * 表单块ID
     */
    @TableField("form_template_sheet_id")
    private Long formTemplateSheetId;
    /**
     * 表单字段ID
     */
    @TableField("form_template_field_id")
    private Long formTemplateFieldId;
    /**
     * 表单字段key
     */
    @TableField("field_key")
    private String fieldKey;
    /**
     * 数据存储方式
     */
    @TableField("form_data_persistence_type")
    private FormDataPersistenceType formDataPersistenceType;
    /**
     * 值
     */
    @TableField("value_")
    private String value;
    /**
     * 批次号
     */
    @TableField("batch_num")
    private Integer batchNum;

}