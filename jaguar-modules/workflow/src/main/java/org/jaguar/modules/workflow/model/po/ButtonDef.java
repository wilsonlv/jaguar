package org.jaguar.modules.workflow.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.core.base.BaseModel;
import org.jaguar.modules.workflow.enums.ButtonActionType;
import org.jaguar.modules.workflow.enums.ButtonPosition;

/**
 * <p>
 * 按钮定义表
 * </p>
 *
 * @author lvws
 * @since 2019-04-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("jaguar_modules_workflow_button_def")
public class ButtonDef extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 按钮名称
     */
    @TableField("name_")
    private String name;
    /**
     * 按钮标签
     */
    @TableField("label_")
    private String label;
    /**
     * 按钮类型（内置：BUILT_IN，附加：ADDON）
     */
    @TableField("button_action_type")
    private ButtonActionType buttonActionType;
    /**
     * 是否默认配置（0：否，1：是）
     */
    @TableField("default_setting")
    private Boolean defaultSetting;
    /**
     * 在哪些页面展示，多个页面逗号分隔（PRE_CREATE，TASK_HANDLE，TASK_VIEW）
     */
    @TableField("show_pages")
    private String showPages;
    /**
     * 按钮位置（BUTTOM，LEFT_TOP，RIGHT_TOP）
     */
    @TableField("button_position")
    private ButtonPosition buttonPosition;
    /**
     * 按钮组件
     */
    @TableField("component_")
    private String component;
    /**
     * 排序号
     */
    @TableField("sort_no")
    private Integer sortNo;
    /**
     * 备注
     */
    @TableField("remark_")
    private String remark;
    /**
     * 备注
     */
    @TableField(exist = false)
    private String params;

}