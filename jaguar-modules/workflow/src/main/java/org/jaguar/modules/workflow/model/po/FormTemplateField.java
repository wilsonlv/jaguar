package org.jaguar.modules.workflow.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.core.base.BaseModel;
import org.jaguar.modules.workflow.enums.FormTemplateFieldType;
import org.jaguar.modules.workflow.model.vo.component.AbstractComponentConfig;

/**
 * <p>
 * 表单字段表
 * </p>
 *
 * @author lvws
 * @since 2019-03-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("jaguar_modules_workflow_form_template_field")
public class FormTemplateField extends BaseModel implements Cloneable {

    private static final long serialVersionUID = 1L;

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
     * 表单行ID
     */
    @TableField("form_template_row_id")
    private Long formTemplateRowId;
    /**
     * 字段标签
     */
    @TableField("label_")
    private String label;
    /**
     * 字段key
     */
    @TableField("key_")
    private String key;
    /**
     * 字段类型
     */
    @TableField("form_template_field_type")
    private FormTemplateFieldType formTemplateFieldType;
    /**
     * 当字段类型为text时，是否显示
     */
    @TableField("visible_")
    private Boolean visible;
    /**
     * 当字段类型为text时，值是否在流程定义中唯一
     */
    @TableField("unique_")
    private Boolean unique;
    /**
     * 提示信息
     */
    @TableField("placeholder_")
    private String placeholder;
    /**
     * 结果或结果表达式
     */
    @TableField("result_value")
    private String resultValue;
    /**
     * 默认值或默认值表达式
     */
    @TableField("default_value")
    private String defaultValue;
    /**
     * 焦点离开事件
     */
    @TableField("foucs_out_event")
    private String foucsOutEvent;
    /**
     * 排序号
     */
    @TableField("sort_no")
    private Integer sortNo;
    /**
     * 数据字典类型
     */
    @TableField("dic_type")
    private String dicType;
    /**
     * 时间格式
     */
    @TableField("time_pattern")
    private String timePattern;
    /**
     * 组件配置
     */
    @TableField("component_config")
    private String componentConfig;

    @TableField(exist = false)
    private AbstractComponentConfig config;

    @TableField(exist = false)
    private Object value;

    @Override
    public FormTemplateField clone() throws CloneNotSupportedException {
        return (FormTemplateField) super.clone();
    }
}