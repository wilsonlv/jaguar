package org.jaguar.modules.workflow.enums;

/**
 * @author lvws
 * @since 2019/4/10.
 */
public enum ProcessOperationType {

    /**
     * 发起
     */
    INITIATE,
    /**
     * 处理
     */
    HANDLE,
    /**
     * 改签
     */
    REASSIGN,
    /**
     * 驳回
     */
    REJECT,
    /**
     * 取消
     */
    CANCEL,
    /**
     * 挂机
     */
    SUSPEND,
    /**
     * 激活
     */
    ACTIVATE,

}
