package org.jaguar.modules.workflow.enums;

/**
 * Created by lvws on 2019/3/14.
 */
public enum FormTemplateFieldType {

    text,//文本框
    number,//数字框
    datetime,//时间
    textarea,//文本域
    staticText,//静态文本（不需要输出，只展示）
    fuzzySearch,//模糊搜索
    radio,//单选按钮
    checkbox,//多选按钮
    dropdown,//下拉选择
    multiCombo,//下拉多选
    pageChoice,//分页选择
    treeChoice,//树形选择
    fileUpload,//文件上传
    urlLink,//url超链接
    userDefined;//自定义组件

}
