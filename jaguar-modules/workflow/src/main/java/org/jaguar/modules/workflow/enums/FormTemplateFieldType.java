package org.jaguar.modules.workflow.enums;

/**
 * @author lvws
 * @since 2019/3/14.
 */
public enum FormTemplateFieldType {

    /**
     * 文本框
     */
    text,
    /**
     * 数字框
     */
    number,
    /**
     * 时间
     */
    datetime,
    /**
     * 文本域
     */
    textarea,
    /**
     * 静态文本（不需要输出，只展示）
     */
    staticText,
    /**
     * 模糊搜索
     */
    fuzzySearch,
    /**
     * 单选按钮
     */
    radio,
    /**
     * 多选按钮
     */
    checkbox,
    /**
     * 下拉选择
     */
    dropdown,
    /**
     * 下拉多选
     */
    multiCombo,
    /**
     * 分页选择
     */
    pageChoice,
    /**
     * 树形选择
     */
    treeChoice,
    /**
     * 文件上传
     */
    fileUpload,
    /**
     * url超链接
     */
    urlLink,
    /**
     * 自定义组件
     */
    userDefined

}
