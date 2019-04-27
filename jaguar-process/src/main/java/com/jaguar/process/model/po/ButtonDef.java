package com.jaguar.process.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jaguar.core.base.BaseModel;
import com.jaguar.process.enums.ButtonActionType;
import com.jaguar.process.enums.ButtonPosition;


/**
 * <p>
 * 按钮定义表
 * </p>
 *
 * @author lvws
 * @since 2019-04-04
 */
@TableName("button_def")
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
     * 按钮动作事件
     */
    @TableField("action_")
    private String action;
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
     * 按钮样式
     */
    @TableField("class_style")
    private String classStyle;
    /**
     * 排序号
     */
    @TableField("sort_no")
    private Integer sortNo;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ButtonActionType getButtonActionType() {
        return buttonActionType;
    }

    public void setButtonActionType(ButtonActionType buttonActionType) {
        this.buttonActionType = buttonActionType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Boolean getDefaultSetting() {
        return defaultSetting;
    }

    public void setDefaultSetting(Boolean defaultSetting) {
        this.defaultSetting = defaultSetting;
    }

    public String getShowPages() {
        return showPages;
    }

    public void setShowPages(String showPages) {
        this.showPages = showPages;
    }

    public ButtonPosition getButtonPosition() {
        return buttonPosition;
    }

    public void setButtonPosition(ButtonPosition buttonPosition) {
        this.buttonPosition = buttonPosition;
    }

    public String getClassStyle() {
        return classStyle;
    }

    public void setClassStyle(String classStyle) {
        this.classStyle = classStyle;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

}